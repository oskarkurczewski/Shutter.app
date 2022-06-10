import React, { useEffect, useState } from "react";
import { PhotographerInfo } from "components/photographer-profile";
import { useGetPhotographerDetailedInfoQuery } from "redux/service/photographerService";
import { useParams } from "react-router-dom";

export const PhotographerProfilePage = () => {
   const { login } = useParams();

   const { data } = useGetPhotographerDetailedInfoQuery(login);

   return (
      <div>
         <p className="category-title">{"photographer_page.category-title"}</p>
         <PhotographerInfo
            name={data?.name}
            surname={data?.surname}
            location="Lodz"
            stars={data?.score}
            sessionCount={30}
            photosCount={546}
            reviewCount={data?.reviewCount}
         />
      </div>
   );
};
