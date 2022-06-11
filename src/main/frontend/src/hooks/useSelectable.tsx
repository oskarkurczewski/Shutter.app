import React, { useCallback, useState } from "react";

export const useSelectable = <T,>({
   objects,
   onSelect,
}: {
   objects: T[];
   onSelect: (selection: T[]) => void;
}) => {
   const [mouseDown, setMouseDown] = useState(false);
   const [selectionStart, setSelectionStart] = useState<number | null>(null);
   const [selectionEnd, setSelectionEnd] = useState<number | null>(null);

   const startSelection = (index: number) => {
      setMouseDown(true);
      setSelectionEnd(index);
      setSelectionStart(index);
      animateSelection(index);
   };

   const animateSelection = (index: number) => {
      if (mouseDown) {
         setSelectionEnd(index);
      }
   };

   const clearSelection = () => {
      setSelectionStart(null);
      setSelectionEnd(null);
   };

   const endSelection = (index: number) => {
      setSelectionEnd(index);
      setMouseDown(false);

      onSelect(objects.slice(selectionStart, selectionEnd + 1));
      clearSelection();
   };

   interface SelectableProps extends React.HTMLAttributes<HTMLDivElement> {
      index: number;
      children?: JSX.Element | JSX.Element[];
      selectedClassName?: string;
      className?: string;
   }

   const Selectable = useCallback(
      ({ index, children, selectedClassName, className, ...rest }: SelectableProps) => {
         const selected =
            selectionStart != null &&
            selectionEnd != null &&
            ((index >= selectionStart && index <= selectionEnd) ||
               (index <= selectionStart && index >= selectionEnd));

         return (
            <div
               className={`${selected ? selectedClassName : ""} ${className}`}
               onMouseDownCapture={() => {
                  startSelection(index);
               }}
               onMouseUpCapture={() => {
                  endSelection(index);
               }}
               onMouseOver={() => {
                  animateSelection(index);
               }}
               onFocus={() => {
                  animateSelection(index);
               }}
               {...rest}
            >
               {children}
            </div>
         );
      },
      [startSelection, endSelection, animateSelection]
   );

   return Selectable;
};
