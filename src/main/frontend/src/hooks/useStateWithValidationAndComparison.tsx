import { useState, useCallback } from "react";
import { useTranslation } from "react-i18next";
import { Condition } from "types";

export const useStateWithValidationAndComparison = <T,>(
   conditions: Condition<T>[],
   initialValues: [T, T]
) => {
   const { t } = useTranslation();

   type stateType = {
      valueA: T;
      valueB: T;
   };
   type validType = {
      valueA: string;
      valueB: string;
   };

   const checkValue = useCallback((value: T, conditions: Condition<T>[]): string => {
      let check = "";
      conditions.forEach((condition, index) => {
         if (check === "" && !condition.function(value)) {
            check = condition.message;
         }
      });
      return check;
   }, []);

   const [state, setState] = useState<stateType>({
      valueA: initialValues[0],
      valueB: initialValues[1],
   });
   const [isValid, setIsValid] = useState<validType>({
      valueA: "",
      valueB: "",
   });

   const onChange = useCallback<(value: stateType) => void>(
      (nextState: stateType) => {
         const value = { ...state, ...nextState };

         setState({ ...state, ...value });
         setIsValid({
            valueA: checkValue(value.valueA, conditions),
            valueB: value.valueA === value.valueB ? "" : t("validator.not_same"),
         });
      },
      [conditions]
   );

   const result: [
      state: {
         valueA: T;
         valueB: T;
      },
      setState: (newState: { valueA?: T; valueB?: T }) => void,
      validation: {
         valueA: string;
         valueB: string;
      }
   ] = [state, onChange, isValid];

   return result;
};
