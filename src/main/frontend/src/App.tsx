import React, { Suspense } from "react";
import "./style.scss";
import { BrowserRouter } from "react-router-dom";
import { SuspenseLoader } from "components/suspense-loader";
import { AppRoutes } from "Routes";

function App() {
   return (
      <Suspense fallback={<SuspenseLoader />}>
         <BrowserRouter>
            <AppRoutes />
         </BrowserRouter>
      </Suspense>
   );
}

export default App;
