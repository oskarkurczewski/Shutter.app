import { useState, useCallback } from "react";

export const useStateWithValidation = <T,>(
   conditions: ((a: T, b?: T) => boolean)[],
   initialValue: T
) => {
   const checkValue = useCallback(
      (value: T, conditions: ((a: T) => boolean)[]): number => {
         let check = null;
         conditions.forEach((condition, index) => {
            if (check === null && !condition(value)) {
               check = index;
            }
         });
         return check;
      },
      []
   );
   const [state, setState] = useState<T>(initialValue);
   const [isValid, setIsValid] = useState<number>(() => checkValue(state, conditions));

   const onChange = useCallback<(value: T) => void>(
      (nextState: T) => {
         const value = typeof nextState === "function" ? nextState(state) : nextState;
         setState(value);
         setIsValid(checkValue(value, conditions));
      },
      [conditions]
   );
   const result: [state: T, setState: (newState: T) => void, validation: number] = [
      state,
      onChange,
      isValid,
   ];
   return result;
};
