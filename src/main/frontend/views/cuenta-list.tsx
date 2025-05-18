import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import {
  Button, Dialog, Grid, GridColumn, GridItemModel,
  NumberField, PasswordField, TextField, VerticalLayout
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import PersonaService from 'Frontend/generated/PersonaService';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useDataProvider } from '@vaadin/hilla-react-crud';
import { useState } from 'react';

export const config: ViewConfig = {
  title: 'Registro de personas',
  menu: {
    icon: 'vaadin:user',
    order: 1,
    title: 'Registro de personas',
  },
};

type Persona = {
  id: string;
  usuario: string;
  edad: string;
  correo: string;
};

function PersonaEntryForm({ onPersonaCreated }: { onPersonaCreated?: () => void }) {
  const usuario = useSignal('');
  const edad = useSignal('');
  const correo = useSignal('');
  const clave = useSignal('');
  const dialogOpened = useSignal(false);

  const createPersona = async () => {
    try {
      if (
        usuario.value.trim() &&
        edad.value.trim() &&
        correo.value.trim() &&
        clave.value.trim()
      ) {
        await PersonaService.save(
          usuario.value,
          correo.value,
          clave.value,
          parseInt(edad.value)
        );
        if (onPersonaCreated) onPersonaCreated();
        usuario.value = '';
        edad.value = '';
        correo.value = '';
        clave.value = '';
        dialogOpened.value = false;
        Notification.show('Persona registrada correctamente', { 
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
    <>
      <Dialog
        modeless
        headerTitle="Nueva persona"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button onClick={() => (dialogOpened.value = false)}>Cancelar</Button>
            <Button onClick={createPersona} theme="primary">Registrar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField 
            label="Nombre de usuario" 
            required
            value={usuario.value} 
            onValueChanged={e => (usuario.value = e.detail.value)} 
          />
          <NumberField 
            label="Edad" 
            required
            min={1}
            value={edad.value} 
            onValueChanged={e => (edad.value = e.detail.value)} 
          />
          <TextField 
            label="Correo electrónico" 
            required
            value={correo.value} 
            onValueChanged={e => (correo.value = e.detail.value)} 
          />
          <PasswordField 
            label="Contraseña" 
            required
            value={clave.value} 
            onValueChanged={e => (clave.value = e.detail.value)} 
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={() => (dialogOpened.value = true)}>
        Agregar persona
      </Button>
    </>
  );
}

function PersonaEditForm({
  persona,
  onPersonaUpdated,
  onClose,
}: {
  persona: Persona | null;
  onPersonaUpdated?: () => void;
  onClose: () => void;
}) {
  const usuario = useSignal(persona?.usuario ?? '');
  const edad = useSignal(persona?.edad ?? '');
  const correo = useSignal(persona?.correo ?? '');
  const clave = useSignal('');

  const updatePersona = async () => {
    try {
      if (
        persona &&
        usuario.value.trim() &&
        edad.value.trim() &&
        correo.value.trim() &&
        clave.value.trim()
      ) {
        await PersonaService.update(
          parseInt(persona.id),
          usuario.value,
          persona.correo,
          clave.value,
          parseInt(edad.value)
        );
        if (onPersonaUpdated) onPersonaUpdated();
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
      headerTitle="Editar persona"
      opened={!!persona}
      onOpenedChanged={({ detail }) => {
        if (!detail.value) onClose();
      }}
      footer={
        <>
          <Button onClick={onClose}>Cancelar</Button>
          <Button onClick={updatePersona} theme="primary">Actualizar</Button>
        </>
      }
    >
      <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
        <TextField 
          label="Nombre de usuario" 
          required
          value={usuario.value} 
          onValueChanged={e => (usuario.value = e.detail.value)} 
        />
        <NumberField 
          label="Edad" 
          required
          min={1}
          value={edad.value} 
          onValueChanged={e => (edad.value = e.detail.value)} 
        />
        <TextField 
          label="Correo electrónico" 
          value={correo.value}
          readonly
          helperText="El correo no se puede modificar"
        />
        <PasswordField 
          label="Nueva contraseña" 
          required
          value={clave.value} 
          onValueChanged={e => (clave.value = e.detail.value)} 
          helperText="Ingrese la nueva contraseña"
        />
      </VerticalLayout>
    </Dialog>
  );
}

export default function PersonaView() {
  const [personaEdit, setPersonaEdit] = useState<Persona | null>(null);
  
  const dataProvider = useDataProvider({
    list: async () => (await PersonaService.listaPersonas()) || [],
  });

  function indexIndex({ model }: { model: GridItemModel<Persona> }) {
    return <span>{model.index + 1}</span>;
  }

  function acciones({ item }: { item: Persona }) {
    return (
      <Button 
        theme="primary"
        onClick={() => setPersonaEdit(item)}
      >
        Editar
      </Button>
    );
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Registro de personas">
        <Group>
          <PersonaEntryForm onPersonaCreated={dataProvider.refresh} />
        </Group>
      </ViewToolbar>

      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn renderer={indexIndex} header="#" width="70px" textAlign="center" />
        <GridColumn path="usuario" header="Usuario" />
        <GridColumn path="edad" header="Edad" width="100px" textAlign="center" />
        <GridColumn path="correo" header="Correo electrónico" />
        <GridColumn header="Acciones" width="120px" renderer={acciones} />
      </Grid>

      {personaEdit && (
        <PersonaEditForm
          persona={personaEdit}
          onPersonaUpdated={dataProvider.refresh}
          onClose={() => setPersonaEdit(null)}
        />
      )}
    </main>
  );
}