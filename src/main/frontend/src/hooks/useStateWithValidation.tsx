import { useState, useCallback, useEffect } from "react";
import { Condition } from "types";

export const useStateWithValidation = <T,>(
   conditions: Condition<T>[],
   initialValue: T
) => {
   const [show, setShow] = useState<boolean>(false);

   const checkValue = useCallback((value: T, conditions: Condition<T>[]): string => {
      let check = "";
      conditions.forEach((condition, index) => {
         if (check === "" && !condition.function(value)) {
            check = condition.message;
         }
      });
      return check;
   }, []);
   const [state, setState] = useState<T>(initialValue);
   const [validationMessage, setValidationMessage] = useState<string>("");

   const onChange = useCallback<(value: T) => void>(
      (nextState: T) => {
         const value = typeof nextState === "function" ? nextState(state) : nextState;
         setState(value);
         setValidationMessage(checkValue(value, conditions));
      },
      [conditions]
   );
   const result: [state: T, setState: (newState: T) => void, validation: string] = [
      state,
      onChange,
      validationMessage,
   ];
   return result;
};
