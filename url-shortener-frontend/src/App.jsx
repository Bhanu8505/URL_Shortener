import { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import { getApps } from "./utils/helper";

function App() {
  const [count, setCount] = useState(0);

  const CurrentApp = getApps();
  return (
    <>
      <BrowserRouter>
        <CurrentApp />
      </BrowserRouter>
    </>
  );
}

export default App;
