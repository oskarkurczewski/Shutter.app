import { useState, useCallback } from "react";

export const useStateWithValidationAndComparison = <T,>(
   conditions: ((a: T, b?: T) => boolean)[],
   initialValue1: T,
   initialValue2: T
) => {
   type stateType = {
      valueA: T;
      valueB: T;
   };
   type validType = {
      valueA: number;
      valueB: boolean;
   };

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

   const [state, setState] = useState<stateType>({
      valueA: initialValue1,
      valueB: initialValue2,
   });
   const [isValid, setIsValid] = useState<validType>({
      valueA: checkValue(state.valueA, conditions),
      valueB: state.valueA === state.valueB,
   });

   const onChange = useCallback<(value: stateType) => void>(
      (nextState: stateType) => {
         const value = nextState;
         setState({ ...state, ...value });
         setIsValid({
            valueA: checkValue(value.valueA, conditions),
            valueB: value.valueA === value.valueB,
         });
      },
      [conditions]
   );

   const result: [
      state: {
         valueA: T;
         valueB: T;
      },
      setState: (newState: { valueA: T; valueB: T }) => void,
      validation: {
         valueA: number;
         valueB: boolean;
      }
   ] = [state, onChange, isValid];

   return result;
};
