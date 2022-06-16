export const getNthParent = (element: HTMLElement, depth: number) => {
   let res = element;
   for (let i = 0; i < depth; i++) {
      res = res.parentElement;
   }
   return res;
};
