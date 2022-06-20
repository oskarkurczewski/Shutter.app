export const nameSurnameFirstLetterPattern = new RegExp(
   "^[A-ZĄĆĘŃŚŁÓŻŹ][a-ząćęńśłóżź]*$"
);
export const nameSurnamePattern = new RegExp("^[A-Za-zĄĆĘŃŚŁÓŻŹąćęńśłóżź]*$");
export const emailPattern = new RegExp(
   "^(?=.{1,64}@)[A-Za-z0-9_-]+(.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$"
);
export const passwordPattern = new RegExp(
   "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()\\-{}:;',?/*~$^+=<>-]).{8,64}$"
);
export const loginPattern = new RegExp("^[<>\\-_=*{}a-zA-Z0-9]$");
export const loginFirstLastPattern = new RegExp(
   "^[a-zA-Z0-9][<>\\-_=*{}a-zA-Z0-9][a-zA-Z0-9]$"
);
