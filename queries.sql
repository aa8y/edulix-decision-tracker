/* GRE new */

/* Min max */
SELECT 
	distinct(university),
	max(GRE_TOTAL),
	min(GRE_TOTAL),
	max(gre_quant),
	min(gre_quant),
	max(gre_verbal),
	min(gre_verbal)
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_TOTAL < 341
AND GRE_TOTAL > 0
AND GRE_QUANT < 171
AND GRE_QUANT > 0
AND GRE_VERBAL < 171
AND GRE_VERBAL > 0
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	max(GRE_TOTAL),
	min(GRE_TOTAL),
	max(gre_quant),
	min(gre_quant),
	max(gre_verbal),
	min(gre_verbal)
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_TOTAL < 1601
AND GRE_TOTAL > 340
AND GRE_QUANT < 801
AND GRE_QUANT > 170
AND GRE_VERBAL < 801
AND GRE_VERBAL > 170
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	MAX(GRE_AWA),
	MIN(GRE_AWA),
	sum(GRE_AWA) / count(*) as GRE_AWA_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_AWA > 0
AND GRE_AWA < 7
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

/* Average */
SELECT 
	distinct(university),
	sum(GRE_TOTAL) / count(*) as GRE_TOTAL_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_TOTAL < 341
AND GRE_TOTAL > 0
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	sum(GRE_TOTAL) / count(*) as GRE_TOTAL_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_TOTAL < 1601
AND GRE_TOTAL > 340
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	sum(GRE_QUANT) / count(*) as GRE_QUANT_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_QUANT < 171
AND GRE_QUANT > 0
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	sum(GRE_QUANT) / count(*) as GRE_QUANT_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_QUANT < 801
AND GRE_QUANT > 170
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	sum(GRE_VERBAL) / count(*) as GRE_VERBAL_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_VERBAL < 171
AND GRE_VERBAL > 0
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

SELECT 
	distinct(university),
	sum(GRE_VERBAL) / count(*) as GRE_VERBAL_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND GRE_VERBAL < 801
AND GRE_VERBAL > 170
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

/* TOEFL */

SELECT 
	distinct(university),
	max(TOEFL),
	min(TOEFL),
	sum(TOEFL) / count(*) as TOEFL_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND TOEFL > 0
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

/* Work_ex */

SELECT 
	distinct(university),
	max(work_ex),
	min(work_ex),
	sum(work_ex) / count(*) as work_ex_avg
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND work_ex > 0
AND work_ex < 100
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

/* Acads */

SELECT 
	distinct(UNIVERSITY),
	max(UG_CGPA),
	min(UG_CGPA),
	sum(UG_CGPA) / count(*) as UG_CGPA_AVG
FROM edulix_test.decision_tracker
WHERE decision = 'admit'
AND UG_CGPA > 0
AND UG_CGPA <= 4
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;

/* # Applications */
SELECT 
	distinct(UNIVERSITY),
	count(*) as ADMITS
FROM edulix_test.decision_tracker
where decision='admit'
GROUP BY UNIVERSITY
ORDER BY UNIVERSITY;