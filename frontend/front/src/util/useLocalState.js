import { useState, useEffect } from 'react';

function useLocalState(defaultValue, key) {
  const [value, setValue] = useState(() => {
    let storedValue = localStorage.getItem(key);

  return storedValue !== null
    ? storedValue
    : defaultValue;
  });

  useEffect(() => {
    localStorage.setItem(key, value);
  }, [key, value]);

  return [value, setValue];
}

export {useLocalState}