import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { ArtistaService, TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import Artista from 'Frontend/generated/org/unl/music/base/models/Artista';
import { useCallback, useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Artistas',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Artistas',
  },
};



type ArtistaEntryFormProps = {
  onArtistaCreated?: () => void;
};

type ArtistaEntryFormPropsUpdate = () => {
  onArtistaUpdated?: () => void;
};
//GUARDAR ARTISTA
function ArtistaEntryForm(props: ArtistaEntryFormProps) {
  const nombre = useSignal('');
  const nacionalidad = useSignal('');
  const createArtista = async () => {
    try {
      if (nombre.value.trim().length > 0 && nacionalidad.value.trim().length > 0) {
        await ArtistaService.createArtista(nombre.value, nacionalidad.value);
        if (props.onArtistaCreated) {
          props.onArtistaCreated();
        }
        nombre.value = '';
        nacionalidad.value = '';
        dialogOpened.value = false;
        Notification.show('Artista creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  let pais = useSignal<String[]>([]);
  useEffect(() => {
    ArtistaService.listCountry().then(data =>
      pais.value = data
    );
  }, []);
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
          <TextField label="Nombre del artista"
            placeholder="Ingrese el nombre del artista"
            aria-label="Nombre del artista"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          <ComboBox label="Nacionalidad"
            items={pais.value}
            placeholder='Seleccione un pais'
            aria-label='Seleccione un pais de la lista'
            value={nacionalidad.value}
            onValueChanged={(evt) => (nacionalidad.value = evt.detail.value)}
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

//GUARDAR ARTISTA
const ArtistaEntryFormUpdate = function (props: ArtistaEntryFormPropsUpdate) {//useCallback((props: ArtistaEntryFormPropsUpdate,{ item: art }: { item: Artista }) => {
  console.log(props);
  let pais = useSignal<String[]>([]);
  useEffect(() => {
    ArtistaService.listCountry().then(data =>
      pais.value = data
    );
  }, []);
  const nombre = useSignal(props.arguments.nombres);
  const nacionalidad = useSignal(props.arguments.nacionalidad);
  const createArtista = async () => {
    try {
      if (nombre.value.trim().length > 0 && nacionalidad.value.trim().length > 0) {
        await ArtistaService.aupdateArtista(props.arguments.id, nombre.value, nacionalidad.value);
        if (props.arguments.onArtistaUpdated) {
          props.arguments.onArtistaUpdated();
        }
        nombre.value = '';
        nacionalidad.value = '';
        dialogOpened.value = false;
        Notification.show('Artista creado', { duration: 5000, position: 'bottom-end', theme: 'success' });
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
        headerTitle="Actualizar artista"
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
          <TextField label="Nombre del artista"
            placeholder="Ingrese el nombre del artista"
            aria-label="Nombre del artista"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          <ComboBox label="Nacionalidad"
            items={pais.value}
            placeholder='Seleccione un pais'
            aria-label='Seleccione un pais de la lista'
            value={nacionalidad.value}
            defaultValue={nacionalidad.value}


            onValueChanged={(evt) => (nacionalidad.value = evt.detail.value)}
          />
        </VerticalLayout>
      </Dialog>
      <Button
        onClick={() => {
          dialogOpened.value = true;
        }}
      >
        Editar
      </Button>
    </>
  );
};


//LISTA DE ARTISTAS
export default function ArtistaView() {
  const [items, setItems] = useState([]);
  const callData = () => {
    console.log("Hola call data");
    ArtistaService.listAll().then(function(data){
      //items.values = data;
      setItems(data);
    });
  };
  useEffect(() => {
    callData();
  },[]);
  
  
  /*let dataProvider = useDataProvider<Artista>({
    list: () => ArtistaService.listAll(),
  });*/

  const order = (event, columnId) => {
    console.log(event);
    const direction = event.detail.value;
    // Custom logic based on the sorting direction
    console.log(`Sort direction changed for column ${columnId} to ${direction}`);

    var dir = (direction == 'asc') ? 1 : 2;
    ArtistaService.order(columnId, dir).then(function (data) {
      setItems(data);
    });  
  }

  function indexLink({ item }: { item: Artista }) {

    return (
      <span>
        <ArtistaEntryFormUpdate  arguments={item}  onArtistaUpdated={callData}>

        </ArtistaEntryFormUpdate>
      </span>
    );
  }

  function indexIndex({ model }: { model: GridItemModel<Artista> }) {
    return (
      <span>
        {model.index + 1}
      </span>
    );
  }

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de artista">
        <Group>
          <ArtistaEntryForm onArtistaCreated={callData}/>
        </Group>
      </ViewToolbar>
      <Grid items={items}>
        <GridColumn renderer={indexIndex} header="Nro" />
        <GridSortColumn path="nombres" header="Nombre del artista" onDirectionChanged={(e) => order(e, 'nombres')} />
        <GridSortColumn path="nacionidad" header="Nacionidad" onDirectionChanged={(e) => order(e, 'nacionalidad')} />

        
        <GridColumn header="Acciones" renderer={indexLink} />
      </Grid>
      
    </main>
  );
}
