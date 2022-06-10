import React, { useEffect, useState } from "react";
import { PhotographerInfo } from "components/photographer-profile";
import { useGetPhotographerDetailedInfoQuery } from "redux/service/photographerService";
import { useParams } from "react-router-dom";

export const PhotographerProfilePage = () => {
   const { login } = useParams();

   // const [info, setInfo] = useState(null);

   const { data } = useGetPhotographerDetailedInfoQuery(login);
   useEffect(() => {
      if (data !== undefined) {
         // data && setInfo(data);
         console.log(data);
      }
   }, [data]);

   return (
      <div>
         {/* {(() => {
            if (isLoading) {
               return <p>{"message.loading.register"}</p>;
            }
            if (isError) {
               return <p className="error">{"message.error.photographer_page"}</p>;
            }
         })()} */}
         <p className="category-title">{"photographer_page.category-title"}</p>
         <PhotographerInfo
            name={data?.name}
            surname={data?.surname}
            location="Lodz"
            stars={data?.score}
            sessionCount={30}
            photosCount={546}
            reviewCount={42}
            likesCount={3324}
         />
      </div>
   );
};
