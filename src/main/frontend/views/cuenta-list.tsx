import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, NumberField, PasswordField, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { ArtistaService, PersonaService, TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Artista from 'Frontend/generated/org/unl/music/base/models/Artista';
import { useCallback, useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Registro de personas',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Registro de personas',
  },
};


type ArtistaEntryFormProps = {
  onArtistaCreated?: () => void;
};

type ArtistaEntryFormPropsUpdate = ()=> {
  onArtistaUpdated?: () => void;
};
//GUARDAR ARTISTA
function ArtistaEntryForm(props: ArtistaEntryFormProps) {
  const usuario = useSignal('');
  const edad = useSignal('');
  const correo = useSignal('');
  const clave = useSignal('');
  const createArtista = async () => {
    try {
      if (usuario.value.trim().length > 0 && edad.value.trim().length > 0
        && correo.value.trim().length > 0 && clave.value.trim().length > 0) {
        await PersonaService.save(usuario.value, correo.value, clave.value, parseInt(edad.value));
        if (props.onArtistaCreated) {
          props.onArtistaCreated();
        }
        usuario.value = '';
        edad.value = '';
        correo.value = '';
        clave.value = '';
        dialogOpened.value = false;
        Notification.show('Persona creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };
  
  
  const dialogOpened = useSignal(false);
  return (
    <>
      <Dialog
        modeless
        headerTitle="Nuevo artista"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button
              onClick={() => {
                dialogOpened.value = false;
              }}
            >
              Candelar
            </Button>
            <Button onClick={createArtista} theme="primary">
              Registrar
            </Button>
            
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre del usuario" 
            placeholder="Ingrese el usuario"
            aria-label="Nombre del usuario"
            value={usuario.value}
            onValueChanged={(evt) => (usuario.value = evt.detail.value)}
          />
          <NumberField  label="Edad del usuario" 
            placeholder="Ingrese la edad del usuario"
            aria-label="Edad del usuario"
            value={edad.value}
            onValueChanged={(evt) => (edad.value = evt.detail.value)}
          />
          <TextField label="Correo del usuario" 
            placeholder="Ingrese el Correo del usuario"
            aria-label="Correo del usuario"
            value={correo.value}
            onValueChanged={(evt) => (correo.value = evt.detail.value)}
          />
          <PasswordField label="Clave del usuario" 
            placeholder="Ingrese la clave del usuario"
            aria-label="Clave del usuario"
            value={clave.value}
            onValueChanged={(evt) => (clave.value = evt.detail.value)}
          />
          
        </VerticalLayout>
      </Dialog>
      <Button
            onClick={() => {
              dialogOpened.value = true;
            }}
          >
            Agregar
          </Button>
    </>
  );
}




//LISTA DE ARTISTAS
export default function ArtistaView() {
  
  const dataProvider = useDataProvider({
    list: () => PersonaService.listaPersonas(),
  });

  function indexLink({ item}: { item: Artista }) {
   
    return (
      <span>
        <Button>EDITAR</Button>
      </span>
    );
  }

  function indexIndex({model}:{model:GridItemModel<Artista>}) {
    return (
      <span>
        {model.index + 1} 
      </span>
    );
  }

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de personas">
        <Group>
          <ArtistaEntryForm onArtistaCreated={dataProvider.refresh}/>
        </Group>
      </ViewToolbar>
      <Grid dataProvider={dataProvider.dataProvider}>
        <GridColumn  renderer={indexIndex} header="Nro" />
        <GridColumn path="usuario" header="Nombre del Usuario" />
        <GridColumn path="edad" header="Edad" />
        <GridColumn path="correo" header="Correo" />
        <GridColumn header="Acciones" renderer={indexLink}/>
      </Grid>
    </main>
  );
}
