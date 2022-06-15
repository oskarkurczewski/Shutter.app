import { IconType } from "react-icons/lib/cjs/iconBase";
import { MdFace, MdPermCameraMic, MdShoppingBasket, MdLandscape } from "react-icons/md";

interface specializationProps {
   color: "purple" | "red" | "blue" | "green";
   icon: IconType;
}
export const getSpecializationProps = (specialization: string): specializationProps => {
   switch (specialization) {
      case "SPECIALIZATION_STUDIO":
         return { color: "purple", icon: MdFace };
      case "SPECIALIZATION_PHOTOREPORT":
         return { color: "red", icon: MdPermCameraMic };
      case "SPECIALIZATION_PRODUCT":
         return { color: "blue", icon: MdShoppingBasket };
      case "SPECIALIZATION_LANDSCAPE":
         return { color: "green", icon: MdLandscape };
   }
};
