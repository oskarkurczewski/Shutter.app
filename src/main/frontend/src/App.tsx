import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "./pages/login/login";

function App() {
   return (
      <BrowserRouter>
         <Routes>
            <Route path="/" element={<LoginPage />} />
         </Routes>
      </BrowserRouter>
   );
}

export default App;
