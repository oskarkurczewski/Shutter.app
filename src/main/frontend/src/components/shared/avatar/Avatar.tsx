import React from "react";
import Gravatar from "react-gravatar";

interface Props {
   email?: string;
   className?: string;
}

export const Avatar: React.FC<Props> = ({ email = "", className = "" }) => {
   return (
      <Gravatar
         className={className}
         email={email}
         default="http://ssbd02.s3.eu-central-1.amazonaws.com/majster2/2fe137fc5d924f57b5a261b9fa0710cd.png"
      />
   );
};
