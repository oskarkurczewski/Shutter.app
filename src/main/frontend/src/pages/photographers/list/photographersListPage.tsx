import React, { useEffect, useState } from "react";
import styles from "./photographersListPage.module.scss";
import { useTranslation } from "react-i18next";
import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from "react-icons/md";
import { Card, Dropdown, TextInput } from "components/shared";
import { FaSearch } from "react-icons/fa";
import { PhotographerListRequest } from "redux/types/api";
import { useGetPhotographerListMutation } from "redux/service/photographerService";
import useDebounce from "hooks/useDebounce";
import {
   DisabledDropdown,
   ListElement,
   AvailabilityFilter,
   SpecializationFilter,
} from "components/photographers-list";
import { AnimatePresence, motion } from "framer-motion";

export const PhotographersListPage = () => {
   const { t } = useTranslation();

   const [selectedSpecialization, setSelectedSpecialization] = useState<string>();
   const [photographerSearchFilters, setPhotographerSearchFilters] =
      useState<PhotographerListRequest>({
         name: undefined,
         specialization: undefined,
         pageNo: 1,
         recordsPerPage: 10,
         weekDay: undefined,
         from: undefined,
         to: undefined,
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
            recordsPerPage: 10,
            list: [],
         },
         isSuccess: getPhotographersSuccess,
      },
   ] = useGetPhotographerListMutation();

   useEffect(() => {
      getPhotographers(debouncedFilters);
   }, [debouncedFilters]);

   useEffect(() => {
      setPhotographerSearchFilters({
         ...photographerSearchFilters,
         specialization: selectedSpecialization,
      });
      getPhotographers(debouncedFilters);
   }, [selectedSpecialization]);

   const handleChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
      setPhotographerSearchFilters({
         ...photographerSearchFilters,
         name: e.target.value,
      });
   };

   const handleDropdownChange: React.ChangeEventHandler<HTMLSelectElement> = (e) => {
      setPhotographerSearchFilters({
         ...photographerSearchFilters,
         recordsPerPage: Number(e.target.value),
      });
   };

   const filterCardContainerVariants = {
      animate: {
         height: expandFilters ? "auto" : 0,
         transition: {
            duration: 0.3,
            type: "tween",
            ease: "easeInOut",
         },
      },
   };

   const filterCardVariants = {
      initial: {
         opacity: 0,
      },
      animate: {
         opacity: 1,
         height: "auto",
         transition: {
            delay: 0.45,
            duration: 0.4,
            ease: "easeOut",
         },
      },
      exit: {
         opacity: 0,
         transition: {
            duration: 0.2,
         },
      },
   };

   return (
      <>
         <div className={`${styles.container}`}>
            <div className={styles.header}>
               <span className="category-title">{t("global.label.filters")}</span>
               <div className={styles.delimiter} />
               <motion.div
                  animate={{
                     rotate: expandFilters ? 90 : 0,
                  }}
               >
                  <button onClick={() => setExpandFilters(!expandFilters)}>
                     <MdKeyboardArrowRight
                        className={expandFilters ? "" : styles.expanded}
                     />
                  </button>
               </motion.div>
            </div>
            <motion.div
               className={`${styles.content}`}
               animate="animate"
               variants={filterCardContainerVariants}
            >
               <AnimatePresence>
                  {expandFilters && (
                     <>
                        <motion.div
                           className={styles.search_filters}
                           key="search-filter"
                           variants={filterCardVariants}
                           initial="initial"
                           animate="animate"
                           exit="exit"
                        >
                           <div className={styles.search_filters_top_row}>
                              <SpecializationFilter
                                 className={styles.card}
                                 selectedSpecialization={selectedSpecialization}
                                 setSelectedSpecialization={setSelectedSpecialization}
                              />
                              <AvailabilityFilter
                                 onChange={setPhotographerSearchFilters}
                                 state={photographerSearchFilters}
                              />
                           </div>
                           <Card className={styles.card}>
                              <p className="section-title">
                                 {t("photographer_list_page.search_photographer")}
                              </p>
                              <TextInput
                                 name="query"
                                 className={styles.input}
                                 icon={<FaSearch />}
                                 value={photographerSearchFilters.name}
                                 onChange={handleChange}
                              />
                           </Card>
                        </motion.div>
                     </>
                  )}
               </AnimatePresence>
            </motion.div>
         </div>

         <div className={styles.container}>
            <div className={styles.header}>
               <span className="category-title">
                  {t("photographer_list_page.photographers")}
               </span>
               <div className={styles.delimiter} />
               <div className={styles.functional}>
                  <p>
                     {getPhotographersSuccess &&
                        t("global.query.results", {
                           count: photographersListResponse.allRecords,
                        })}
                  </p>
                  <DisabledDropdown
                     label={t("photographer_list_page.list")}
                     className={styles.disabled_dropdown}
                  />
                  <DisabledDropdown
                     label={t("photographer_list_page.az")}
                     className={styles.disabled_dropdown}
                  />
                  <Dropdown
                     values={["10", "25", "50", "100"]}
                     selectedValue={photographerSearchFilters.recordsPerPage}
                     name="recordsPerPage"
                     onChange={handleDropdownChange}
                     id="recordsPerPage"
                     className={styles.dropdown}
                  />
                  <div className={styles.pagination_controls}>
                     <button
                        disabled={photographerSearchFilters.pageNo === 1}
                        onClick={() => {
                           setPhotographerSearchFilters({
                              ...photographerSearchFilters,
                              pageNo: photographerSearchFilters.pageNo - 1,
                           });
                        }}
                     >
                        <MdKeyboardArrowLeft />
                        <span className="label">poprz.</span>
                     </button>
                     <span className="label-bold">
                        {photographerSearchFilters.pageNo}
                     </span>
                     <button
                        disabled={
                           photographerSearchFilters.pageNo ===
                           photographersListResponse.allPages
                        }
                        onClick={() => {
                           setPhotographerSearchFilters({
                              ...photographerSearchFilters,
                              pageNo: photographerSearchFilters.pageNo + 1,
                           });
                        }}
                     >
                        <span className="label">nast.</span>
                        <MdKeyboardArrowRight />
                     </button>
                  </div>
               </div>
            </div>
            <div className={`${styles.content} ${styles.list}`}>
               <AnimatePresence exitBeforeEnter>
                  {getPhotographersSuccess &&
                     photographersListResponse.list?.map((obj, i) => (
                        <ListElement
                           custom={i}
                           data={obj}
                           key={obj.login}
                           styles={styles}
                        />
                     ))}
               </AnimatePresence>
            </div>
         </div>
      </>
   );
};
