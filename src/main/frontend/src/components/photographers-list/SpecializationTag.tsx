import { IconText } from "components/shared";
import React, { FC } from "react";
import { Specialization } from "types/Specializations";

interface SpecializationTagProps {
   text: string;
   specialization?: Specialization;
   className: string;
}

export const SpecializationTag: FC<SpecializationTagProps> = ({
   text,
   specialization,
   className,
}) => {
   switch (specialization) {
      case Specialization.SPECIALIZATION_LANDSCAPE:
         return <IconText className={className} color="green" text={text} />;
      case Specialization.SPECIALIZATION_PHOTOREPORT:
         return <IconText className={className} color="red" text={text} />;
      case Specialization.SPECIALIZATION_PRODUCT:
         return <IconText className={className} color="blue" text={text} />;
      case Specialization.SPECIALIZATION_STUDIO:
         return <IconText className={className} color="purple" text={text} />;
      default:
         return <IconText className={className} text="..." />;
   }
};
