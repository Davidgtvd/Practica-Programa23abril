import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import {
  Button, ComboBox, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn,
  HorizontalLayout, Icon, NumberField, Select, TextField, VerticalLayout
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { CancionServices } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Gestión de Canciones',
  menu: {
    icon: 'vaadin:music',
    order: 2,
    title: 'Canciones',
  },
};

type Cancion = {
  id: string;
  nombre: string;
  genero: string;
  album: string;
  duracion: string;
  duracionFormateada?: string;
  url: string;
  tipo: string;
  artistaBanda?: string;
};

// FORMULARIO DE CREAR
type CancionEntryFormProps = {
  onCancionCreated?: () => void;
};

function CancionEntryForm(props: CancionEntryFormProps) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');
  const duracion = useSignal(0);
  const url = useSignal('');
  const tipo = useSignal('MP3');
  const artistaBanda = useSignal('');

  const tiposArchivo = [
    { label: 'MP3', value: 'MP3' },
    { label: 'WAV', value: 'WAV' },
    { label: 'FLAC', value: 'FLAC' },
    { label: 'AAC', value: 'AAC' },
    { label: 'OGG', value: 'OGG' },
  ];

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
    // Limpiar campos
    nombre.value = '';
    genero.value = '';
    album.value = '';
    duracion.value = 0;
    url.value = '';
    tipo.value = 'MP3';
    artistaBanda.value = '';
  };

  const createCancion = async () => {
    try {
      if (
        nombre.value.trim().length > 0 &&
        genero.value.trim().length > 0 &&
        album.value.trim().length > 0 &&
        duracion.value > 0 &&
        url.value.trim().length > 0 &&
        artistaBanda.value.trim().length > 0
      ) {
        await CancionServices.create(
          nombre.value.trim(),
          genero.value.trim(),
          duracion.value,
          url.value.trim(),
          tipo.value,
          album.value.trim(),
          artistaBanda.value.trim()
        );

        if (props.onCancionCreated) {
          props.onCancionCreated();
        }

        close();
        Notification.show('Canción creada exitosamente', {
          duration: 5000,
          position: 'bottom-end',
          theme: 'success'
        });
      } else {
        Notification.show('Todos los campos son obligatorios', {
          duration: 5000,
          position: 'top-center',
          theme: 'error'
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Canción"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Registrar Canción
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createCancion}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '400px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <TextField
            label="Nombre de la canción"
            placeholder="Ingrese el nombre de la canción"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            required
          />

          <TextField
            label="Género"
            placeholder="Ingrese el género musical"
            value={genero.value}
            onValueChanged={(evt) => (genero.value = evt.detail.value)}
            required
          />

          <TextField
            label="Álbum"
            placeholder="Ingrese el nombre del álbum"
            value={album.value}
            onValueChanged={(evt) => (album.value = evt.detail.value)}
            required
          />

          <TextField
            label="Artista/Banda"
            placeholder="Ingrese el nombre del artista o banda"
            value={artistaBanda.value}
            onValueChanged={(evt) => (artistaBanda.value = evt.detail.value)}
            required
          />

          <NumberField
            label="Duración (segundos)"
            placeholder="Duración en segundos"
            value={duracion.value.toString()}
            onValueChanged={(evt) => (duracion.value = parseInt(evt.detail.value) || 0)}
            min={1}
            required
          />

          <TextField
            label="URL"
            placeholder="URL del archivo de audio"
            value={url.value}
            onValueChanged={(evt) => (url.value = evt.detail.value)}
            required
          />

          <ComboBox
            label="Tipo de archivo"
            items={tiposArchivo}
            value={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">Registrar Canción</Button>
    </>
  );
}

//FORMULARIO PARA LA EDICION
type CancionEditFormProps = {
  cancion: Cancion;
  onCancionUpdated?: () => void;
};

function CancionEditForm(props: CancionEditFormProps) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal(props.cancion.nombre);
  const genero = useSignal(props.cancion.genero);
  const album = useSignal(props.cancion.album);
  const duracion = useSignal(parseInt(props.cancion.duracion));
  const url = useSignal(props.cancion.url);
  const tipo = useSignal(props.cancion.tipo);
  const artistaBanda = useSignal(props.cancion.artistaBanda ?? '');

  useEffect(() => {
    nombre.value = props.cancion.nombre;
    genero.value = props.cancion.genero;
    album.value = props.cancion.album;
    duracion.value = parseInt(props.cancion.duracion);
    url.value = props.cancion.url;
    tipo.value = props.cancion.tipo;
    artistaBanda.value = props.cancion.artistaBanda ?? '';
  }, [props.cancion]);

  const tiposArchivo = [
    { label: 'MP3', value: 'MP3' },
    { label: 'WAV', value: 'WAV' },
    { label: 'FLAC', value: 'FLAC' },
    { label: 'AAC', value: 'AAC' },
    { label: 'OGG', value: 'OGG' },
  ];

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const updateCancion = async () => {
    try {
      if (
        nombre.value.trim().length > 0 &&
        genero.value.trim().length > 0 &&
        album.value.trim().length > 0 &&
        duracion.value > 0 &&
        url.value.trim().length > 0 &&
        artistaBanda.value.trim().length > 0
      ) {
        await CancionServices.update(
          parseInt(props.cancion.id),
          nombre.value.trim(),
          genero.value.trim(),
          duracion.value,
          url.value.trim(),
          tipo.value,
          album.value.trim(),
          artistaBanda.value.trim()
        );

        if (props.onCancionUpdated) {
          props.onCancionUpdated();
        }

        close();
        Notification.show('Canción actualizada exitosamente', {
          duration: 5000,
          position: 'bottom-end',
          theme: 'success'
        });
      } else {
        Notification.show('Todos los campos son obligatorios', {
          duration: 5000,
          position: 'top-center',
          theme: 'error'
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  const deleteCancion = async () => {
    try {
      if (confirm('¿Está seguro de que desea eliminar esta canción?')) {
        await CancionServices.delete(parseInt(props.cancion.id));

        if (props.onCancionUpdated) {
          props.onCancionUpdated();
        }

        close();
        Notification.show('Canción eliminada exitosamente', {
          duration: 5000,
          position: 'bottom-end',
          theme: 'success'
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Canción"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Editar Canción
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="error" onClick={deleteCancion}>
              Eliminar
            </Button>
            <Button theme="primary" onClick={updateCancion}>
              Actualizar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '400px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <TextField
            label="Nombre de la canción"
            placeholder="Ingrese el nombre de la canción"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            required
          />

          <TextField
            label="Género"
            placeholder="Ingrese el género musical"
            value={genero.value}
            onValueChanged={(evt) => (genero.value = evt.detail.value)}
            required
          />

          <TextField
            label="Álbum"
            placeholder="Ingrese el nombre del álbum"
            value={album.value}
            onValueChanged={(evt) => (album.value = evt.detail.value)}
            required
          />

          <TextField
            label="Artista/Banda"
            placeholder="Ingrese el nombre del artista o banda"
            value={artistaBanda.value}
            onValueChanged={(evt) => (artistaBanda.value = evt.detail.value)}
            required
          />

          <NumberField
            label="Duración (segundos)"
            placeholder="Duración en segundos"
            value={duracion.value.toString()}
            onValueChanged={(evt) => (duracion.value = parseInt(evt.detail.value) || 0)}
            min={1}
            required
          />

          <TextField
            label="URL"
            placeholder="URL del archivo de audio"
            value={url.value}
            onValueChanged={(evt) => (url.value = evt.detail.value)}
            required
          />

          <ComboBox
            label="Tipo de archivo"
            items={tiposArchivo}
            value={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">
        Editar
      </Button>
    </>
  );
}

export default function CancionListView() {
  const [items, setItems] = useState<Cancion[]>([]);
  const [allItems, setAllItems] = useState<Cancion[]>([]);

  // Carga LOS DATO PARA INICIAR
  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const data = await CancionServices.listAll();
      const filteredData = (data ?? []).filter(Boolean);
      setItems(filteredData);
      setAllItems(filteredData); 
    } catch (error) {
      handleError(error);
    }
  };

  // ordenacin
  const order = (event: CustomEvent, columnId: string) => {
    const direction = (event as any).detail.value;
    const dir = (direction === 'asc') ? 1 : 2;
    CancionServices.order(columnId, dir).then(function (data) {
      const filteredData = (data ?? []).filter(Boolean);
      setItems(filteredData);
      setAllItems(filteredData); 
    });
  };

  //  busqueda
  const criterio = useSignal('');
  const texto = useSignal('');

  const itemSelect = [
    { label: 'Nombre', value: 'nombre' },
    { label: 'Género', value: 'genero' },
    { label: 'Álbum', value: 'album' },
    { label: 'Tipo', value: 'tipo' },
    { label: 'Artista/Banda', value: 'artistaBanda' },
  ];

  //  BuSQUEDA DE ESPECIFICO 
  const search = async () => {
    try {
      if (!criterio.value || !texto.value.trim()) {
        Notification.show('Seleccione un criterio e ingrese texto a buscar', {
          duration: 3000,
          position: 'top-center',
          theme: 'error'
        });
        return;
      }

      const searchText = texto.value.trim().toLowerCase();

      // Filtrar 
      const filteredResults = allItems.filter(item => {
        let fieldValue = '';

        switch (criterio.value) {
          case 'nombre':
            fieldValue = item.nombre?.toLowerCase() || '';
            break;
          case 'genero':
            fieldValue = item.genero?.toLowerCase() || '';
            break;
          case 'album':
            fieldValue = item.album?.toLowerCase() || '';
            break;
          case 'tipo':
            fieldValue = item.tipo?.toLowerCase() || '';
            break;
          case 'artistaBanda':
            fieldValue = item.artistaBanda?.toLowerCase() || '';
            break;
          default:
            return false;
        }

                return fieldValue.startsWith(searchText);
      });

      setItems(filteredResults);

      // 🆕 Mostrar mensaje según resultados
      if (filteredResults.length === 0) {
        Notification.show(`No se encontraron canciones que empiecen con "${texto.value}" en ${criterio.value}`, {
          duration: 5000,
          position: 'top-center',
          theme: 'contrast'
        });
      } else {
        Notification.show(`Se encontraron ${filteredResults.length} resultado(s)`, {
          duration: 3000,
          position: 'bottom-end',
          theme: 'success'
        });
      }

    } catch (error) {
      handleError(error);
    }
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      search();
    }
  };

  const showAll = async () => {
    try {
      setItems(allItems); 
      criterio.value = '';
      texto.value = '';
      Notification.show(`Mostrando todos los registros (${allItems.length})`, {
        duration: 2000,
        position: 'bottom-end',
        theme: 'success'
      });
    } catch (error) {
      handleError(error);
    }
  };

  function renderIndex({ model }: { model: GridItemModel<Cancion> }) {
    return <span>{model.index + 1}</span>;
  }

  function renderActions({ item }: { item: Cancion }) {
    return (
      <CancionEditForm
        cancion={item}
        onCancionUpdated={loadData}
      />
    );
  }

  function renderDuracion({ item }: { item: Cancion }) {
    return (
      <span>
        {item.duracionFormateada || `${item.duracion}s`}
      </span>
    );
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Gestión de Canciones">
        <Group>
          <CancionEntryForm onCancionCreated={loadData} />
        </Group>
      </ViewToolbar>

      {/* Panel de búsqueda */}
      <HorizontalLayout theme="spacing">
        <Select
          items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder="Seleccione un criterio"
        />

        <TextField
          placeholder="Buscar por letra o palabra..."
          style={{ width: '50%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}
          onKeyDown={onKeyDown}
        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>

        <Button onClick={search} theme="primary">
          BUSCAR
        </Button>

        <Button onClick={showAll} theme="secondary">
          MOSTRAR TODOS
        </Button>
      </HorizontalLayout>

      {/* Grid con datos */}
      <Grid items={items}>
        <GridColumn header="Nro" renderer={renderIndex} width="70px" />
        <GridSortColumn
          onDirectionChanged={(e) => order(e, "nombre")}
          path="nombre"
          header="Canción"
        />
        <GridSortColumn
          path="genero"
          header="Género"
          onDirectionChanged={(e) => order(e, "genero")}
        />
        <GridSortColumn
          path="album"
          header="Álbum"
          onDirectionChanged={(e) => order(e, "album")}
        />
        <GridSortColumn
          path="artistaBanda"
          header="Artista/Banda"
          onDirectionChanged={(e) => order(e, "artistaBanda")}
        />
        <GridColumn
          header="Duración"
          renderer={renderDuracion}
          width="120px"
        />
        <GridColumn path="tipo" header="Tipo" width="80px" />
        <GridColumn header="Acciones" renderer={renderActions} width="100px" />
      </Grid>

      <div style={{
        marginTop: '1rem',
        fontSize: '0.9rem',
        color: 'var(--lumo-secondary-text-color)'
      }}>
        Total de registros: {items.length}
        {items.length !== allItems.length && (
          <span style={{ marginLeft: '10px', fontStyle: 'italic' }}>
            (de {allItems.length} totales)
          </span>
        )}
      </div>
    </main>
  );
}