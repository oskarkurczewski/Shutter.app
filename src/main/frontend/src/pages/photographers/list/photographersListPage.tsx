import React, { useEffect, useState } from "react";
import styles from "./photographersListPage.module.scss";
import { useTranslation } from "react-i18next";
import { MdKeyboardArrowRight } from "react-icons/md";
import { Card, TextInput } from "components/shared";
import { FaSearch } from "react-icons/fa";
import { PhotographerListRequest } from "redux/types/api";
import { useGetPhotographerListMutation } from "redux/service/photographerService";
import useDebounce from "hooks/useDebounce";
import { DisabledDropdown, ListElement } from "components/photographers-list";
import { AnimatePresence, motion } from "framer-motion";

export const PhotographersListPage = () => {
   const { t } = useTranslation();

   const [photographerSearchFilters, setPhotographerSearchFilters] =
      useState<PhotographerListRequest>({
         query: "",
         pageNo: 1,
         recordsPerPage: 25,
      });
   const [expandFilters, setExpandFilters] = useState(true);

   const debouncedFilters = useDebounce<PhotographerListRequest>(
      photographerSearchFilters,
      200
   );

   const [
      getPhotographers,
      {
         data: photographersListResponse = {
            allPages: 0,
            allRecords: 0,
            pageNo: 1,
            recordsPerPage: 25,
            list: [],
         },
      },
   ] = useGetPhotographerListMutation();

   useEffect(() => {
      getPhotographers(photographerSearchFilters);
   }, [debouncedFilters]);

   const handleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
      const name = e.target.name;
      setPhotographerSearchFilters({
         ...photographerSearchFilters,
         [name]: e.target.value,
      });
   };

   const variants = {
      initial: { opacity: 0 },
      visible: { opacity: 1 },
   };

   return (
      <>
         <section className={`${styles.container}`}>
            <div className={styles.header}>
               <span className="category-title">{t("global.label.filters")}</span>
               <div className={styles.delimiter} />
               <button onClick={() => setExpandFilters(!expandFilters)}>
                  <MdKeyboardArrowRight
                     className={expandFilters ? "" : styles.expanded}
                  />
               </button>
            </div>
            <div className={`${styles.content} ${expandFilters ? "" : styles.hidden}`}>
               <Card className={styles.card}>
                  <p className="section-title">
                     {t("photographer_list_page.search_photographer")}
                  </p>
                  <TextInput
                     name="query"
                     className={styles.input}
                     icon={<FaSearch />}
                     value={photographerSearchFilters.query}
                     onChange={handleChange}
                  />
               </Card>
            </div>
         </section>

         <div className={styles.container}>
            <div className={styles.header}>
               <span className="category-title">
                  {t("photographer_list_page.photographers")}
               </span>
               <div className={styles.delimiter} />
               <div className={styles.functional}>
                  <p>{t("photographer_list_page.results", { count: 69 })}</p>
                  <DisabledDropdown label="LIST" className={styles.dropdown} />
                  <DisabledDropdown label="A-Z" className={styles.dropdown} />
               </div>
            </div>
            <div className={`${styles.content} ${styles.list}`}>
               <AnimatePresence>
                  {photographersListResponse.list?.map((obj, i) => (
                     <ListElement custom={i} data={obj} key={obj.login} styles={styles} />
                  ))}
               </AnimatePresence>
            </div>
         </div>
      </>
   );
};
