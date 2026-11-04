import React, { useState } from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';

export default function App() {
  const [count, setCount] = useState(0);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>React Native</Text>

      <Text style={styles.counter}>Laskurin arvo: {count}</Text>

      <View style={styles.buttons}>
        <Button title="-" onPress={() => setCount(Math.max(0, count - 1))} />

        <Button title="Reset" onPress={() => setCount(0)} />

        <Button title="+" onPress={() => setCount(count + 1)} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 32,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    marginBottom: 24,
  },	
  counter: {
    fontSize: 22,
    marginBottom: 24,
  },
  buttons: {
    flexDirection: 'row',
    gap: 12,
  },
});
