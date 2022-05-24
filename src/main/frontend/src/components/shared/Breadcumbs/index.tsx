import React from "react";
import "./style.scss";
import { BsFillCaretLeftFill } from "react-icons/bs";
import { Link, useLocation, useNavigate } from "react-router-dom";

const Breadcumbs = () => {
   const location = useLocation();
   const navigate = useNavigate();

   console.log(location);

   let fullURL = "";
   const crumbs = location.pathname
      .split("/")
      .filter((crumb) => crumb !== "")
      .map((crumb) => {
         fullURL += `${crumb}/`;
         return {
            fullURL,
            url: crumb,
         };
      });

   return (
      <div className="breadcumbs-wrapper">
         <BsFillCaretLeftFill onClick={() => navigate(-1)} />
         {crumbs.map((crumb, index) => (
            <>
               <Link to={crumb.fullURL} key={index}>
                  {crumb.url}
               </Link>
               <span>/</span>
            </>
         ))}
      </div>
   );
};

export default Breadcumbs;
