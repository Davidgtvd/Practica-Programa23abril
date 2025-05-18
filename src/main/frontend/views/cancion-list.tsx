import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import {
  Button, ComboBox, Dialog, Grid, GridColumn, GridItemModel,
  NumberField, TextField, VerticalLayout
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { CancionServices } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useDataProvider } from '@vaadin/hilla-react-crud';
import { useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Canciones',
  menu: {
    icon: 'vaadin:music',
    order: 1,
    title: 'Canciones',
  },
};

type Cancion = {
  id: number;
  nombre: string;
  genero: string;
  album: string;
  url: string;
  tipo: string;
  duracion: number;
};

function CancionEntryForm({ onCancionCreated }: { onCancionCreated?: () => void }) {
  const nombre = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  const dialogOpened = useSignal(false);
  const tipos = useSignal<string[]>([]);

  useEffect(() => {
    CancionServices.listarTiposArchivo().then(data => {
      tipos.value = data || [];
    });
  }, []);

  const createCancion = async () => {
    try {
      if (
        nombre.value.trim() &&
        genero.value.trim() &&
        album.value.trim() &&
        duracion.value.trim() &&
        url.value.trim() &&
        tipo.value
      ) {
        await CancionServices.create(
          nombre.value,
          genero.value,
          parseInt(duracion.value),
          url.value,
          tipo.value,
          album.value
        );
        if (onCancionCreated) onCancionCreated();
        nombre.value = '';
        genero.value = '';
        album.value = '';
        duracion.value = '';
        url.value = '';
        tipo.value = '';
        dialogOpened.value = false;
        Notification.show('Canción registrada correctamente', { 
          duration: 3000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('Todos los campos son requeridos', { 
          duration: 3000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  const GENEROS = [
    'Rock', 'Pop', 'Jazz', 'Clásica', 'Reggaeton', 'Metal'
  ];

  return (
    <>
      <Dialog
        modeless
        headerTitle="Nueva canción"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button onClick={() => (dialogOpened.value = false)}>Cancelar</Button>
            <Button onClick={createCancion} theme="primary">Registrar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField 
            label="Nombre de la canción" 
            required
            value={nombre.value} 
            onValueChanged={e => (nombre.value = e.detail.value)} 
          />
          <ComboBox 
            label="Género" 
            required
            items={GENEROS}
            value={genero.value}
            onValueChanged={e => (genero.value = e.detail.value)}
          />
          <TextField 
            label="Álbum" 
            required
            value={album.value} 
            onValueChanged={e => (album.value = e.detail.value)} 
          />
          <ComboBox 
            label="Tipo" 
            required
            items={tipos.value}
            value={tipo.value}
            onValueChanged={e => (tipo.value = e.detail.value)}
          />
          <NumberField 
            label="Duración (segundos)" 
            required
            min={1}
            value={duracion.value} 
            onValueChanged={e => (duracion.value = e.detail.value)} 
          />
          <TextField 
            label="URL" 
            required
            value={url.value} 
            onValueChanged={e => (url.value = e.detail.value)} 
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={() => (dialogOpened.value = true)}>
        Agregar canción
      </Button>
    </>
  );
}

function CancionEditForm({
  cancion,
  onCancionUpdated,
  onClose,
}: {
  cancion: Cancion | null;
  onCancionUpdated?: () => void;
  onClose: () => void;
}) {
  const nombre = useSignal(cancion?.nombre ?? '');
  const genero = useSignal(cancion?.genero ?? '');
  const album = useSignal(cancion?.album ?? '');
  const duracion = useSignal(cancion?.duracion?.toString() ?? '');
  const url = useSignal(cancion?.url ?? '');
  const tipo = useSignal(cancion?.tipo ?? '');
  const tipos = useSignal<string[]>([]);

  useEffect(() => {
    CancionServices.listarTiposArchivo().then(data => {
      tipos.value = data || [];
    });
  }, []);

  const GENEROS = [
    'Rock', 'Pop', 'Jazz', 'Clásica', 'Reggaeton', 'Metal'
  ];

  const updateCancion = async () => {
    try {
      if (
        cancion &&
        nombre.value.trim() &&
        genero.value.trim() &&
        album.value.trim() &&
        duracion.value.trim() &&
        url.value.trim() &&
        tipo.value
      ) {
        await CancionServices.update(
          cancion.id,
          nombre.value,
          genero.value,
          parseInt(duracion.value),
          url.value,
          tipo.value,
          album.value
        );
        if (onCancionUpdated) onCancionUpdated();
        onClose();
        Notification.show('Datos actualizados correctamente', { 
          duration: 3000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('Todos los campos son requeridos', { 
          duration: 3000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <Dialog
      modeless
      headerTitle="Editar canción"
      opened={!!cancion}
      onOpenedChanged={({ detail }) => {
        if (!detail.value) onClose();
      }}
      footer={
        <>
          <Button onClick={onClose}>Cancelar</Button>
          <Button onClick={updateCancion} theme="primary">Actualizar</Button>
        </>
      }
    >
      <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
        <TextField 
          label="Nombre de la canción" 
          required
          value={nombre.value} 
          onValueChanged={e => (nombre.value = e.detail.value)} 
        />
        <ComboBox 
          label="Género" 
          required
          items={GENEROS}
          value={genero.value}
          onValueChanged={e => (genero.value = e.detail.value)}
        />
        <TextField 
          label="Álbum" 
          required
          value={album.value} 
          onValueChanged={e => (album.value = e.detail.value)} 
        />
        <ComboBox 
          label="Tipo" 
          required
          items={tipos.value}
          value={tipo.value}
          onValueChanged={e => (tipo.value = e.detail.value)}
        />
        <NumberField 
          label="Duración (segundos)" 
          required
          min={1}
          value={duracion.value} 
          onValueChanged={e => (duracion.value = e.detail.value)} 
        />
        <TextField 
          label="URL" 
          required
          value={url.value} 
          onValueChanged={e => (url.value = e.detail.value)} 
        />
      </VerticalLayout>
    </Dialog>
  );
}

export default function CancionView() {
  const [cancionEdit, setCancionEdit] = useState<Cancion | null>(null);

  const dataProvider = useDataProvider<Cancion>({
    list: async () => (await CancionServices.listarCanciones()) || [],
  });

  function renderIndex({ model }: { model: GridItemModel<Cancion> }) {
    return <span>{model.index + 1}</span>;
  }

  function renderActions({ item }: { item: Cancion }) {
    return (
      <Button 
        theme="primary"
        onClick={() => setCancionEdit(item)}
      >
        Editar
      </Button>
    );
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Lista de canciones">
        <Group>
          <CancionEntryForm onCancionCreated={dataProvider.refresh} />
        </Group>
      </ViewToolbar>

      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn renderer={renderIndex} header="#" width="70px" textAlign="center" />
        <GridColumn path="nombre" header="Canción" />
        <GridColumn path="genero" header="Género" />
        <GridColumn path="album" header="Álbum" />
        <GridColumn path="duracion" header="Duración" />
        <GridColumn path="url" header="URL" />
        <GridColumn path="tipo" header="Tipo" />
        <GridColumn header="Acciones" width="120px" renderer={renderActions} />
      </Grid>

      {cancionEdit && (
        <CancionEditForm
          cancion={cancionEdit}
          onCancionUpdated={dataProvider.refresh}
          onClose={() => setCancionEdit(null)}
        />
      )}
    </main>
  );
}