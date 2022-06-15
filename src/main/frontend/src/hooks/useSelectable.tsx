import React, { useCallback, useState } from "react";

interface SelectableProps extends React.HTMLAttributes<HTMLDivElement> {
   index: number;
   disabled?: boolean;
   children?: JSX.Element | JSX.Element[];
   selectedClassName?: string;
   className?: string;
}

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

   const Selectable = useCallback(
      ({
         index,
         disabled = false,
         children,
         selectedClassName,
         className,
         ...rest
      }: SelectableProps) => {
         const selected =
            selectionStart != null &&
            selectionEnd != null &&
            ((index >= selectionStart && index <= selectionEnd) ||
               (index <= selectionStart && index >= selectionEnd));

         return (
            <div
               className={`${
                  selected && !disabled ? selectedClassName : ""
               }  ${className}`}
               onMouseDownCapture={() => {
                  !disabled && startSelection(index);
               }}
               onMouseUpCapture={() => {
                  !disabled && endSelection(index);
               }}
               onMouseOver={() => {
                  !disabled && animateSelection(index);
               }}
               onFocus={() => {
                  !disabled && animateSelection(index);
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
