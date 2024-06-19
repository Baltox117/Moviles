import react, { useState } from "react";
import { View, StyleSheet, Dimensions, TouchableOpacity, Text, style } from "react-native";
import { Svg, Path } from "react-native-svg";

const { height, width } = Dimensions.get('window');

export default () => {
  const [paths, setPaths] = useState([]);
  const [currentPath, setCurrentPath] = useState([]);
  const [isClearButtonClicked, setClearButtonClicked] = useState([]);

  const onTouchEnd = () => {
    if (currentPath.length > 0) {
      setPaths([...paths, currentPath.join('')]);
      setCurrentPath([]);
    }
    setClearButtonClicked(false);
  };

  const onTouchMove = (event) => {
    const locationX = event.nativeEvent.locationX;
    const locationY = event.nativeEvent.locationY;
    const newPoint = `${currentPath.length === 0 ? 'M' :
      'L'}${locationX.toFixed(0)},${locationY.toFixed(0)}`;
    setCurrentPath([...currentPath, newPoint]);
  };

  const handleClearButtonClick = () => {
    setPaths([]);
    setCurrentPath([]);
    setClearButtonClicked(true);
  };

  return (
    <View style = {styles.container}>
      <View style = {styles.svgContainer} onTouchMove = {onTouchMove} onTouchEnd = {onTouchEnd}>
        <Svg height={height * 0.7} width={width}>
          {paths.map((path, index) => (
            <Path
            key = {`path.${index}`}
            d = {path}
            stroke = "red"
            fill = "transparent"
            strokeWidth = {3}
            strokeLinejoin = "round"
            strokeLinecap = "round"
            />
          ))}
          {currentPath.length > 0 && (
            <Path
            d = {currentPath.join('')}
            stroke = "red"
            fill = "transparent"
            strokeWidth = {3}
            strokeLinejoin = "round"
            strokeLinecap = "round"
            />
          )}
        </Svg>
      </View>
      <TouchableOpacity style = {styles.clearButton} onPress = {handleClearButtonClick}>
        <Text style = {styles.clearButtonText}>Clear</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  svgContainer: {
    height: height * 7,
    width,
    borderColor: 'black',
    backgroundColor: 'white',
    borderWidth: 1,
  },
  clearButton: {
    marginTop: 10,
    backgroundColor: 'black',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 5,
  },
  clearButtonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
});