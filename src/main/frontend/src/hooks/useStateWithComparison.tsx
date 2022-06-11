import { useState, useCallback } from "react";

export const useStateWithComparison = <T,>(initialValue: T, comparedValue: T) => {
   const [state, setState] = useState<T>(initialValue);
   const [isValid, setIsValid] = useState<boolean>(() => state === comparedValue);

   const onChange = useCallback<(value: T) => void>(
      (nextState: T) => {
         const value = typeof nextState === "function" ? nextState(state) : nextState;
         setState(value);
         setIsValid(value === comparedValue);
      },
      [comparedValue, isValid]
   );
   const result: [state: T, setState: (newState: T) => void, validation: boolean] = [
      state,
      onChange,
      isValid,
   ];
   return result;
};
