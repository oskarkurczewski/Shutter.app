import React from "react";
import { PhotographerInfo } from "components/photographer-profile";

export const PhotographerProfilePage = () => {
   return (
      <div>
         <PhotographerInfo
            name="Potężny"
            surname="Fotografer"
            location="Lodz"
            stars={1}
            sessionCount={30}
            photosCount={546}
            reviewCount={42}
            likesCount={3324}
         />
      </div>
   );
};
