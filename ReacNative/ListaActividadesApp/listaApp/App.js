import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import Actividades from './components/Actividades'

export default function App() {
  return (
    <View style={styles.container}>
      {/*Actividades del dia*/}
      <View style = {styles.taskWrapper}>
        <Text style = {styles.sectionTitle}>Actividades del dia</Text>
        <View style = {styles.items}>
          {/*Aqui van las actividades del dia*/}
          <Actividades text = {'Actividad 1'}/>
          <Actividades text = {'Actividad 2'}/>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#9EECFF',
  },
  taskWrapper: {
    paddingTop: 80,
    paddingHorizontal: 20,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: 'bold',
  },
  items: {
    marginTop: 30,
  },
});
