import React, { useEffect, useRef } from "react";
import { createPortal } from "react-dom";

const modalRoot = document.getElementById("modal") as HTMLElement;

const ModalRoot: React.FC = ({ children }) => {
   const elRef = useRef(document.createElement("div"));

   useEffect(() => {
      modalRoot.appendChild(elRef.current);
      return () => {
         modalRoot.removeChild(elRef.current);
      };
   }, []);

   return createPortal(<>{children}</>, elRef.current);
};

export default ModalRoot;
