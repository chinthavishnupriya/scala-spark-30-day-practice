# Day 30 Final Educational Capstone — Actual Ubuntu Output

> Captured from the user's Ubuntu/WSL terminal on 29 September 2026. The output below is preserved as received; warnings and streaming connection errors are intentionally retained.

```text
==============================================
DAY 30 - EDUCATION ANALYTICS CAPSTONE
==============================================
Raw assessment records: 2400
Valid assessment records: 2365
Accumulator value: 2365
Enriched records: 2365
26/09/29 08:54:48 WARN SparkStringUtils: Truncated the string representation of a plan since it was too large. This behavior can be adjusted by setting 'spark.sql.debug.maxToStringFields'.
Enriched partitions: 4
--- Pair RDD: Total Marks by Course ---
C01 -> 10766.00
C02 -> 10492.00
C03 -> 10495.00
C04 -> 10548.00
C05 -> 10525.00
C06 -> 10557.00
C07 -> 10299.00
C08 -> 10579.00
--- Spark SQL: Student Performance ---
+----------+------------+--------+-------+-----------+-------------+
|student_id|student_name|semester|section|assessments|average_score|
+----------+------------+--------+-------+-----------+-------------+
|S167      |Priya_167   |4       |A      |7          |80.71        |
|S033      |Meera_33    |4       |A      |7          |79.71        |
|S026      |Rohan_26    |4       |B      |8          |78.38        |
|S126      |Rohan_126   |4       |B      |8          |78.38        |
|S267      |Priya_267   |4       |A      |8          |78.13        |
|S233      |Meera_233   |4       |A      |8          |77.75        |
|S133      |Meera_133   |4       |A      |8          |77.75        |
|S142      |Rahul_142   |4       |B      |7          |77.57        |
|S049      |Sneha_49    |4       |A      |8          |77.38        |
|S149      |Sneha_149   |4       |A      |8          |77.38        |
|S249      |Sneha_249   |4       |A      |8          |77.38        |
|S253      |Meera_253   |4       |A      |8          |77.25        |
|S053      |Meera_53    |4       |A      |8          |77.25        |
|S153      |Meera_153   |4       |A      |8          |77.25        |
|S008      |Vikram_8    |4       |B      |7          |77.14        |
|S090      |Aditya_90   |4       |B      |8          |77.13        |
|S190      |Aditya_190  |4       |B      |8          |77.13        |
|S290      |Aditya_290  |4       |B      |8          |77.13        |
|S274      |Arjun_274   |4       |B      |8          |76.88        |
|S074      |Arjun_74    |4       |B      |8          |76.88        |
+----------+------------+--------+-------+-----------+-------------+
only showing top 20 rows
--- Window: Ranking Students within Each Course ---
+---------+----------------+----------+------------+---------------+---------+----------------+-----------+
|course_id|course_name     |student_id|student_name|assessment_type|score_pct|performance_band|course_rank|
+---------+----------------+----------+------------+---------------+---------+----------------+-----------+
|C01      |Data Engineering|S035      |Kavya_35    |QUIZ2          |96.0     |Excellent       |1          |
|C01      |Data Engineering|S076      |Rohan_76    |QUIZ1          |96.0     |Excellent       |2          |
|C01      |Data Engineering|S079      |Sneha_79    |MID2           |96.0     |Excellent       |3          |
|C01      |Data Engineering|S115      |Kavya_115   |QUIZ2          |96.0     |Excellent       |4          |
|C01      |Data Engineering|S120      |Aditya_120  |MID1           |96.0     |Excellent       |5          |
|C01      |Data Engineering|S156      |Rohan_156   |QUIZ1          |96.0     |Excellent       |6          |
|C01      |Data Engineering|S235      |Kavya_235   |QUIZ2          |96.0     |Excellent       |7          |
|C01      |Data Engineering|S276      |Rohan_276   |QUIZ1          |96.0     |Excellent       |8          |
|C01      |Data Engineering|S279      |Sneha_279   |MID2           |96.0     |Excellent       |9          |
|C01      |Data Engineering|S033      |Meera_33    |PROJECT        |95.0     |Excellent       |10         |
|C01      |Data Engineering|S038      |Vikram_38   |LAB1           |95.0     |Excellent       |11         |
|C01      |Data Engineering|S074      |Arjun_74    |FINAL          |95.0     |Excellent       |12         |
|C01      |Data Engineering|S117      |Priya_117   |LAB2           |95.0     |Excellent       |13         |
|C01      |Data Engineering|S158      |Vikram_158  |LAB1           |95.0     |Excellent       |14         |
|C01      |Data Engineering|S197      |Priya_197   |LAB2           |95.0     |Excellent       |15         |
|C01      |Data Engineering|S233      |Meera_233   |PROJECT        |95.0     |Excellent       |16         |
|C01      |Data Engineering|S238      |Vikram_238  |LAB1           |95.0     |Excellent       |17         |
|C01      |Data Engineering|S274      |Arjun_274   |FINAL          |95.0     |Excellent       |18         |
|C01      |Data Engineering|S040      |Aditya_40   |MID1           |94.0     |Excellent       |19         |
|C01      |Data Engineering|S153      |Meera_153   |PROJECT        |94.0     |Excellent       |20         |
+---------+----------------+----------+------------+---------------+---------+----------------+-----------+
only showing top 20 rows
--- Physical Plan ---
== Physical Plan ==
AdaptiveSparkPlan (69)
+- HashAggregate (68)
   +- Exchange (67)
      +- HashAggregate (66)
         +- InMemoryTableScan (1)
               +- InMemoryRelation (2)
                     +- AdaptiveSparkPlan (65)
                        +- == Final Plan ==
                           ResultQueryStage (49)
                           +- * Project (48)
                              +- * BroadcastHashJoin Inner BuildRight (47)
                                 :- * Project (35)
                                 :  +- * BroadcastHashJoin Inner BuildRight (34)
                                 :     :- ShuffleQueryStage (13), Statistics(sizeInBytes=299.8 KiB, rowCount=2.37E+3)
                                 :     :  +- Exchange (12)
                                 :     :     +- * Filter (11)
                                 :     :        +- TableCacheQueryStage (10), Statistics(sizeInBytes=182.8 KiB, rowCount=2.37E+3)
                                 :     :           +- InMemoryTableScan (3)
                                 :     :                 +- InMemoryRelation (4)
                                 :     :                       +- * Project (9)
                                 :     :                          +- * Project (8)
                                 :     :                             +- * Project (7)
                                 :     :                                +- * Filter (6)
                                 :     :                                   +- Scan csv  (5)
                                 :     +- BroadcastQueryStage (33), Statistics(sizeInBytes=8.0 MiB, rowCount=8)
                                 :        +- BroadcastExchange (32)
                                 :           +- * Filter (31)
                                 :              +- TableCacheQueryStage (30), Statistics(sizeInBytes=310.0 B, rowCount=8
                                 :                 +- InMemoryTableScan (14)
                                 :                       +- InMemoryRelation (15)
                                 :                             +- AdaptiveSparkPlan (29)
                                       +- == Final Plan ==
                                          ResultQueryStage (23)
                                          +- SortAggregate (22)
                                             +- * Sort (21)
                                                +- ShuffleQueryStage (20), Statistics(sizeInBytes=792.0 B, rowCount=8)
                                                   +- Exchange (19)
                                                      +- SortAggregate (18)
                                                         +- * Sort (17)
                                                            +- Scan csv  (16)
                                       +- == Initial Plan ==
                                          SortAggregate (28)
                                          +- Sort (27)
                                             +- Exchange (26)
                                                +- SortAggregate (25)
                                                   +- Sort (24)
                                                      +- Scan csv  (16)
                                 +- BroadcastQueryStage (46), Statistics(sizeInBytes=8.0 MiB, rowCount=300)
                                    +- BroadcastExchange (45)
                                       +- SortAggregate (44)
                                          +- * Sort (43)
                                             +- AQEShuffleRead (42), coalesced
                                                +- ShuffleQueryStage (41), Statistics(sizeInBytes=27.5 KiB, rowCount=300
                                                   +- Exchange (40)
                                                      +- SortAggregate (39)
                                                         +- * Sort (38)
                                                            +- * Filter (37)
                                                               +- Scan csv  (36)
                        +- == Initial Plan ==
                           Project (64)
                           +- BroadcastHashJoin Inner BuildRight (63)
                              :- Project (55)
                              :  +- BroadcastHashJoin Inner BuildRight (54)
                              :     :- Exchange (51)
                              :     :  +- Filter (50)
                              :     :     +- InMemoryTableScan (3)
                              :     :           +- InMemoryRelation (4)
                              :     :                 +- * Project (9)
                              :     :                    +- * Project (8)
                              :     :                       +- * Project (7)
                              :     :                          +- * Filter (6)
                              :     :                             +- Scan csv  (5)
                              :     +- BroadcastExchange (53)
                              :        +- Filter (52)
                              :           +- InMemoryTableScan (14)
                              :                 +- InMemoryRelation (15)
                              :                       +- AdaptiveSparkPlan (29)
                              +- == Final Plan ==
                                 ResultQueryStage (23)
                                 +- SortAggregate (22)
                                    +- * Sort (21)
                                       +- ShuffleQueryStage (20), Statistics(sizeInBytes=792.0 B, rowCount=8)
                                          +- Exchange (19)
                                             +- SortAggregate (18)
                                                +- * Sort (17)
                                                   +- Scan csv  (16)
                              +- == Initial Plan ==
                                 SortAggregate (28)
                                 +- Sort (27)
                                    +- Exchange (26)
                                       +- SortAggregate (25)
                                          +- Sort (24)
                                             +- Scan csv  (16)
                              +- BroadcastExchange (62)
                                 +- SortAggregate (61)
                                    +- Sort (60)
                                       +- Exchange (59)
                                          +- SortAggregate (58)
                                             +- Sort (57
                                                +- Filter (56)
                                                   +- Scan csv  (36)
(1) InMemoryTableScan
Output [2]: [score_pct#74, department#44]
Arguments: [score_pct#74, department#44]
(2) InMemoryRelation
Arguments: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_name#64, semester#65, section#66], StorageLevel(disk, memory, deserialized, 1 replicas)
(3) InMemoryTableScan
Output [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Arguments: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77], [isnotnull(course_id#18), isnotnull(student_id#17)]
(4) InMemoryRelation
Arguments: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77], StorageLevel(disk, memory, deserialized, 1 replicas)
(5) Scan csv
Output [8]: [student_id#17, course_id#18, assessment_date#19, assessment_type#20, marks#21, max_marks#22, attendance_pct#23, status#24]
Batched: false
Location: InMemoryFileIndex [file:/home/vishnupriya/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone/input/assessments.csv]
PushedFilters: [IsNotNull(max_marks), IsNotNull(marks), IsNotNull(student_id), IsNotNull(course_id), IsNotNull(assessment_date), GreaterThan(max_marks,0), GreaterThanOrEqual(marks,0)]
ReadSchema: struct<student_id:string,course_id:string,assessment_date:date,assessment_type:string,marks:int,max_marks:int,attendance_pct:int,status:string>
(6) Filter [codegen id : 1]
Input [8]: [student_id#17, course_id#18, assessment_date#19, assessment_type#20, marks#21, max_marks#22, attendance_pct#23, status#24]
Condition : ((((((((isnotnull(max_marks#22) AND isnotnull(marks#21)) AND isnotnull(student_id#17)) AND isnotnull(course_id#18)) AND isnotnull(assessment_date#19)) AND (max_marks#22 > 0)) AND (marks#21 >= 0)) AND (cast(marks#21 as double) <= cast(max_marks#22 as double))) AND (upper(trim(status#24, None)) = VALID))
(7) Project [codegen id : 1]
Output [8]: [student_id#17, course_id#18, assessment_date#19, upper(trim(assessment_type#20, None)) AS assessment_type#72, cast(marks#21 as double) AS marks#69, cast(max_marks#22 as double) AS max_marks#70, cast(attendance_pct#23 as double) AS attendance_pct#71, upper(trim(status#24, None)) AS status#73]
Input [8]: [student_id#17, course_id#18, assessment_date#19, assessment_type#20, marks#21, max_marks#22, attendance_pct#23, status#24]
(8) Project [codegen id : 1]
Output [9]: [student_id#17, course_id#18, assessment_date#19, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, round(((marks#69 / max_marks#70) * 100.0), 2) AS score_pct#74]
Input [8]: [student_id#17, course_id#18, assessment_date#19, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73]
(9) Project [codegen id : 1]
Output [10]: [student_id#17, course_id#18, assessment_date#19, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, if (isnull(score_pct#74)) null else UDF(knownnotnull(score_pct#74)) AS performance_band#77]
Input [9]: [student_id#17, course_id#18, assessment_date#19, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74]
(10) TableCacheQueryStage
Output [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Arguments: 0
(11) Filter [codegen id : 2]
Input [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Condition : (isnotnull(course_id#18) AND isnotnull(student_id#17))
(12) Exchange
Input [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Arguments: hashpartitioning(student_id#17, 4), REPARTITION_BY_NUM, [plan_id=381]
(13) ShuffleQueryStage
Output [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Arguments: 3
(14) InMemoryTableScan
Output [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: [course_id#42, course_name#43, department#44, credits#45], [isnotnull(course_id#42)]
(15) InMemoryRelation
Arguments: [course_id#42, course_name#43, department#44, credits#45], StorageLevel(disk, memory, deserialized, 1 replicas)
(16) Scan csv
Output [4]: [course_id#42, course_name#43, department#44, credits#45]
Batched: false
Location: InMemoryFileIndex [file:/home/vishnupriya/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone/input/courses.csv]
ReadSchema: struct<course_id:string,course_name:string,department:string,credits:int>
(17) Sort [codegen id : 1]
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: [course_id#42 ASC NULLS FIRST], false, 0
(18) SortAggregate
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Keys [1]: [course_id#42]
Functions [3]: [partial_first(course_name#43, false), partial_first(department#44, false), partial_first(credits#45, false)]
Aggregate Attributes [6]: [first#653, valueSet#654, first#655, valueSet#656, first#657, valueSet#658]
Results [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
(19) Exchange
Input [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Arguments: hashpartitioning(course_id#42, 200), ENSURE_REQUIREMENTS, [plan_id=329]
(20) ShuffleQueryStage
Output [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Arguments: 0
(21) Sort [codegen id : 2]
Input [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Arguments: [course_id#42 ASC NULLS FIRST], false, 0
(22) SortAggregate
Input [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Keys [1]: [course_id#42]
Functions [3]: [first(course_name#43, false), first(department#44, false), first(credits#45, false)]
Aggregate Attributes [3]: [first(course_name#43)()#647, first(department#44)()#649, first(credits#45)()#651]
Results [4]: [course_id#42, first(course_name#43)()#647 AS course_name#648, first(department#44)()#649 AS department#650, first(credits#45)()#651 AS credits#652]
(23) ResultQueryStage
Output [4]: [course_id#42, course_name#648, department#650, credits#652]
Arguments: 1
(24) Sort
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: [course_id#42 ASC NULLS FIRST], false, 0
(25) SortAggregate
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Keys [1]: [course_id#42]
Functions [3]: [partial_first(course_name#43, false), partial_first(department#44, false), partial_first(credits#45, false)]
Aggregate Attributes [6]: [first#653, valueSet#654, first#655, valueSet#656, first#657, valueSet#658]
Results [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
(26) Exchange
Input [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Arguments: hashpartitioning(course_id#42, 200), ENSURE_REQUIREMENTS, [plan_id=177]
(27) Sort
Input [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Arguments: [course_id#42 ASC NULLS FIRST], false, 0
(28) SortAggregate
Input [7]: [course_id#42, first#659, valueSet#660, first#661, valueSet#662, first#663, valueSet#664]
Keys [1]: [course_id#42]
Functions [3]: [first(course_name#43, false), first(department#44, false), first(credits#45, false)]
Aggregate Attributes [3]: [first(course_name#43)()#647, first(department#44)()#649, first(credits#45)()#651]
Results [4]: [course_id#42, first(course_name#43)()#647 AS course_name#648, first(department#44)()#649 AS department#650, first(credits#45)()#651 AS credits#652]
(29) AdaptiveSparkPlan
Output [4]: [course_id#42, course_name#648, department#650, credits#652]
Arguments: isFinalPlan=true
(30) TableCacheQueryStage
Output [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: 1
(31) Filter [codegen id : 3]
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Condition : isnotnull(course_id#42)
(32) BroadcastExchange
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: HashedRelationBroadcastMode(List(input[0, string, false]),false), [plan_id=427]
(33) BroadcastQueryStage
Output [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: 4
(34) BroadcastHashJoin [codegen id : 5]
Left keys [1]: [course_id#18]
Right keys [1]: [course_id#42]
Join type: Inner
Join condition: None
(35) Project [codegen id : 5]
Output [13]: [course_id#18, student_id#17, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45]
Input [14]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_id#42, course_name#43, department#44, credits#45]
(36) Scan csv
Output [4]: [student_id#63, student_name#64, semester#65, section#66]
Batched: false
Location: InMemoryFileIndex [file:/home/vishnupriya/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone/input/student-profiles.csv]
PushedFilters: [IsNotNull(student_id)]
ReadSchema: struct<student_id:string,student_name:string,semester:int,section:string>
(37) Filter [codegen id : 1]
Input [4]: [student_id#63, student_name#64, semester#65, section#66]
Condition : isnotnull(student_id#63)
(38) Sort [codegen id : 1]
Input [4]: [student_id#63, student_name#64, semester#65, section#66]
Arguments: [student_id#63 ASC NULLS FIRST], false, 0
(39) SortAggregate
Input [4]: [student_id#63, student_name#64, semester#65, section#66]
Keys [1]: [student_id#63]
Functions [3]: [partial_first(student_name#64, false), partial_first(semester#65, false), partial_first(section#66, false)]
Aggregate Attributes [6]: [first#922, valueSet#923, first#924, valueSet#925, first#926, valueSet#927]
Results [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
(40) Exchange
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Arguments: hashpartitioning(student_id#63, 200), ENSURE_REQUIREMENTS, [plan_id=301]
(41) ShuffleQueryStage
Output [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Arguments: 2
(42) AQEShuffleRead
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Arguments: coalesced
(43) Sort [codegen id : 4]
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Arguments: [student_id#63 ASC NULLS FIRST], false, 0
(44) SortAggregate
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Keys [1]: [student_id#63]
Functions [3]: [first(student_name#64, false), first(semester#65, false), first(section#66, false)]
Aggregate Attributes [3]: [first(student_name#64)()#846, first(semester#65)()#848, first(section#66)()#850]
Results [4]: [student_id#63, first(student_name#64)()#846 AS student_name#847, first(semester#65)()#848 AS semester#849, first(section#66)()#850 AS section#851]
(45) BroadcastExchange
Input [4]: [student_id#63, student_name#847, semester#849, section#851]
Arguments: HashedRelationBroadcastMode(List(input[0, string, true]),false), [plan_id=485]
(46) BroadcastQueryStage
Output [4]: [student_id#63, student_name#847, semester#849, section#851]
Arguments: 5
(47) BroadcastHashJoin [codegen id : 5]
Left keys [1]: [student_id#17]
Right keys [1]: [student_id#63]
Join type: Inner
Join condition: None
(48) Project [codegen id : 5]
Output [16]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_name#847, semester#849, section#851]
Input [17]: [course_id#18, student_id#17, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_id#63, student_name#847, semester#849, section#851]
(49) ResultQueryStage
Output [16]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_name#847, semester#849, section#851]
Arguments: 6
(50) Filter
Input [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Condition : (isnotnull(course_id#18) AND isnotnull(student_id#17))
(51) Exchange
Input [10]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77]
Arguments: hashpartitioning(student_id#17, 4), REPARTITION_BY_NUM, [plan_id=214]
(52) Filter
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Condition : isnotnull(course_id#42)
(53) BroadcastExchange
Input [4]: [course_id#42, course_name#43, department#44, credits#45]
Arguments: HashedRelationBroadcastMode(List(input[0, string, false]),false), [plan_id=231]
(54) BroadcastHashJoin
Left keys [1]: [course_id#18]
Right keys [1]: [course_id#42]
Join type: Inner
Join condition: None
(55) Project
Output [13]: [course_id#18, student_id#17, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45]
Input [14]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_id#42, course_name#43, department#44, credits#45]
(56) Filter
Input [4]: [student_id#63, student_name#64, semester#65, section#66]
Condition : isnotnull(student_id#63)
(57) Sort
Input [4]: [student_id#63, student_name#64, semester#65, section#66]
Arguments: [student_id#63 ASC NULLS FIRST], false, 0
(58) SortAggregate
Input [4]: [student_id#63, student_name#64, semester#65, section#66]
Keys [1]: [student_id#63]
Functions [3]: [partial_first(student_name#64, false), partial_first(semester#65, false), partial_first(section#66, false)]
Aggregate Attributes [6]: [first#922, valueSet#923, first#924, valueSet#925, first#926, valueSet#927]
Results [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
(59) Exchange
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Arguments: hashpartitioning(student_id#63, 200), ENSURE_REQUIREMENTS, [plan_id=237]
(60) Sort
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Arguments: [student_id#63 ASC NULLS FIRST], false, 0
(61) SortAggregate
Input [7]: [student_id#63, first#928, valueSet#929, first#930, valueSet#931, first#932, valueSet#933]
Keys [1]: [student_id#63]
Functions [3]: [first(student_name#64, false), first(semester#65, false), first(section#66, false)]
Aggregate Attributes [3]: [first(student_name#64)()#846, first(semester#65)()#848, first(section#66)()#850]
Results [4]: [student_id#63, first(student_name#64)()#846 AS student_name#847, first(semester#65)()#848 AS semester#849, first(section#66)()#850 AS section#851]
(62) BroadcastExchange
Input [4]: [student_id#63, student_name#847, semester#849, section#851]
Arguments: HashedRelationBroadcastMode(List(input[0, string, true]),false), [plan_id=241]
(63) BroadcastHashJoin
Left keys [1]: [student_id#17]
Right keys [1]: [student_id#63]
Join type: Inner
Join condition: None
(64) Project
Output [16]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_name#847, semester#849, section#851]
Input [17]: [course_id#18, student_id#17, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_id#63, student_name#847, semester#849, section#851]
(65) AdaptiveSparkPlan
Output [16]: [student_id#17, course_id#18, assessment_date#68, assessment_type#72, marks#69, max_marks#70, attendance_pct#71, status#73, score_pct#74, performance_band#77, course_name#43, department#44, credits#45, student_name#847, semester#849, section#851]
Arguments: isFinalPlan=true
(66) HashAggregate
Input [2]: [score_pct#74, department#44]
Keys [1]: [department#44]
Functions [1]: [partial_avg(score_pct#74)]
Aggregate Attributes [2]: [sum#3021, count#3022L]
Results [3]: [department#44, sum#3023, count#3024L]
(67) Exchange
Input [3]: [department#44, sum#3023, count#3024L]
Arguments: hashpartitioning(department#44, 200), ENSURE_REQUIREMENTS, [plan_id=704]
(68) HashAggregate
Input [3]: [department#44, sum#3023, count#3024L]
Keys [1]: [department#44]
Functions [1]: [avg(score_pct#74)]
Aggregate Attributes [1]: [avg(score_pct#74)#2780]
Results [2]: [department#44, round(avg(score_pct#74)#2780, 2) AS average_score#2763]
(69) AdaptiveSparkPlan
Output [2]: [department#44, average_score#2763]
Arguments: isFinalPlan=false
[success] Total time: 22 s, completed Sep 29, 2026, 8:54:50 AM
vishnupriya@vishnupriya:~/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone$ sbt "run streaming"
welcome to sbt 1.10.11 (Ubuntu Java 17.0.20.1)
loading project definition from /home/vishnupriya/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone/project
loading settings for project root from build.sbt...
set current project to day30-education-analytics-capstone (in build file:/home/vishnupriya/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone/)
running (fork) Day30 streaming
Using Spark's default log4j profile: org/apache/spark/log4j2-defaults.properties
26/09/29 08:55:06 WARN Utils: Your hostname, vishnupriya, resolves to a loopback address: 127.0.1.1; using 10.255.255.254 instead (on interface lo)
26/09/29 08:55:06 WARN Utils: Set SPARK_LOCAL_IP if you need to bind to another address
26/09/29 08:55:07 INFO SparkContext: Running Spark version 4.2.0
26/09/29 08:55:07 INFO SparkContext: OS info Linux, 6.18.33.2-microsoft-standard-WSL2, amd64
26/09/29 08:55:07 INFO SparkContext: Java version 17.0.20.1+1-1-26.04-Ubuntu
26/09/29 08:55:07 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
26/09/29 08:55:07 INFO ResourceUtils: ==============================================================
26/09/29 08:55:07 INFO ResourceUtils: No custom resources configured for spark.driver.
26/09/29 08:55:07 INFO ResourceUtils: ==============================================================
26/09/29 08:55:07 INFO SparkContext: Submitted application: Day 30 - Education Analytics Capstone
26/09/29 08:55:07 INFO SecurityManager: Changing view acls to: vishnupriya
26/09/29 08:55:07 INFO SecurityManager: Changing modify acls to: vishnupriya
26/09/29 08:55:07 INFO SecurityManager: Changing view acls groups to: vishnupriya
26/09/29 08:55:07 INFO SecurityManager: Changing modify acls groups to: vishnupriya
26/09/29 08:55:07 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: vishnupriya groups with view permissions: EMPTY; users with modify permissions: vishnupriya; groups with modify permissions: EMPTY; RPC SSL disabled
26/09/29 08:55:07 INFO Utils: Successfully started service 'sparkDriver' on port 39973.
26/09/29 08:55:07 INFO SparkEnv: Registering MapOutputTracker
26/09/29 08:55:07 INFO SparkEnv: Registering BlockManagerMaster
26/09/29 08:55:07 INFO BlockManagerMasterEndpoint: Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
26/09/29 08:55:07 INFO BlockManagerMasterEndpoint: BlockManagerMasterEndpoint up
26/09/29 08:55:07 INFO SparkEnv: Registering BlockManagerMasterHeartbeat
26/09/29 08:55:07 INFO DiskBlockManager: Created local directory at /tmp/blockmgr-b31c782a-2712-44f0-b81a-51482d3291b4
26/09/29 08:55:07 INFO SparkEnv: Registering OutputCommitCoordinator
26/09/29 08:55:08 INFO JettyUtils: Start Jetty 0.0.0.0:4040 for SparkUI
26/09/29 08:55:08 INFO Utils: Successfully started service 'SparkUI' on port 4040.
26/09/29 08:55:08 INFO ResourceProfile: Default ResourceProfile created, executor resources: Map(cores -> name: cores, amount: 1, script: , vendor: , memory -> name: memory, amount: 1024, script: , vendor: , offHeap -> name: offHeap, amount: 0, script: , vendor: ), task resources: Map(cpus -> name: cpus, amount: 1.0)
26/09/29 08:55:08 INFO ResourceProfile: Limiting resource is cpu
26/09/29 08:55:08 INFO ResourceProfileManager: Added ResourceProfile id: 0
26/09/29 08:55:08 INFO SecurityManager: Changing view acls to: vishnupriya
26/09/29 08:55:08 INFO SecurityManager: Changing modify acls to: vishnupriya
26/09/29 08:55:08 INFO SecurityManager: Changing view acls groups to: vishnupriya
26/09/29 08:55:08 INFO SecurityManager: Changing modify acls groups to: vishnupriya
26/09/29 08:55:08 INFO SecurityManager: SecurityManager: authentication disabled; ui acls disabled; users with view permissions: vishnupriya groups with view permissions: EMPTY; users with modify permissions: vishnupriya; groups with modify permissions: EMPTY; RPC SSL disabled
26/09/29 08:55:08 INFO Executor: Starting executor ID driver on host 10.255.255.254
26/09/29 08:55:08 INFO Executor: Running Spark version 4.2.0
26/09/29 08:55:08 INFO Executor: OS info Linux, 6.18.33.2-microsoft-standard-WSL2, amd64
26/09/29 08:55:08 INFO Executor: Java version 17.0.20.1+1-1-26.04-Ubuntu
26/09/29 08:55:08 INFO Executor: Starting executor with user classpath (userClassPathFirst = false): ''
26/09/29 08:55:08 INFO Executor: Created or updated repl class loader org.apache.spark.util.MutableURLClassLoader@4416e18d for default.
26/09/29 08:55:08 INFO Utils: Successfully started service 'org.apache.spark.network.netty.NettyBlockTransferService' on port 45475.
26/09/29 08:55:08 INFO NettyBlockTransferService: Server created on 10.255.255.254:45475
26/09/29 08:55:08 INFO BlockManager: Using org.apache.spark.storage.RandomBlockReplicationPolicy for block replication policy
26/09/29 08:55:08 INFO BlockManagerMaster: Registering BlockManager BlockManagerId(driver, 10.255.255.254, 45475, None)
26/09/29 08:55:08 INFO BlockManagerMasterEndpoint: Registering block manager 10.255.255.254:45475 with 987.6 MiB RAM, BlockManagerId(driver, 10.255.255.254, 45475, None)
26/09/29 08:55:08 INFO BlockManagerMaster: Registered BlockManager BlockManagerId(driver, 10.255.255.254, 45475, None)
26/09/29 08:55:08 INFO BlockManager: Initialized BlockManager: BlockManagerId(driver, 10.255.255.254, 45475, None)
--- Streaming component started on localhost:9998 ---
Send: cat input/attendance-events.txt | nc localhost 999
Batch interval: 5 seconds; state uses updateStateByKey.
26/09/29 08:55:10 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.start(ReceiverSupervisor.scala:132)
        at org.apache.spark.streaming.scheduler.ReceiverTracker$ReceiverTrackerEndpoint.$anonfun$startReceiver$1(ReceiverTracker.scala:603)
        at org.apache.spark.streaming.scheduler.ReceiverTracker$ReceiverTrackerEndpoint.$anonfun$startReceiver$1$adapted(ReceiverTracker.scala:593)
        at org.apache.spark.SparkContext.$anonfun$submitJob$1(SparkContext.scala:2646)
        at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:93)
        at org.apache.spark.TaskContext.runTaskWithListeners(TaskContext.scala:206)
        at org.apache.spark.scheduler.Task.run(Task.scala:147)
        at org.apache.spark.executor.Executor$TaskRunner.$anonfun$run$4(Executor.scala:894)
        at org.apache.spark.util.SparkErrorUtils.tryWithSafeFinally(SparkErrorUtils.scala:86)
        at org.apache.spark.util.SparkErrorUtils.tryWithSafeFinally$(SparkErrorUtils.scala:83)
        at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:97)
        at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:897)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:10 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.start(ReceiverSupervisor.scala:132)
        at org.apache.spark.streaming.scheduler.ReceiverTracker$ReceiverTrackerEndpoint.$anonfun$startReceiver$1(ReceiverTracker.scala:603)
        at org.apache.spark.streaming.scheduler.ReceiverTracker$ReceiverTrackerEndpoint.$anonfun$startReceiver$1$adapted(ReceiverTracker.scala:593)
        at org.apache.spark.SparkContext.$anonfun$submitJob$1(SparkContext.scala:2646)
        at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:93)
        at org.apache.spark.TaskContext.runTaskWithListeners(TaskContext.scala:206)
        at org.apache.spark.scheduler.Task.run(Task.scala:147)
        at org.apache.spark.executor.Executor$TaskRunner.$anonfun$run$4(Executor.scala:894)
        at org.apache.spark.util.SparkErrorUtils.tryWithSafeFinally(SparkErrorUtils.scala:86)
        at org.apache.spark.util.SparkErrorUtils.tryWithSafeFinally$(SparkErrorUtils.scala:83)
        at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:97)
        at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:897)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:12 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:12 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:14 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:14 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:16 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:16 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:18 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:18 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:20 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:20 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:22 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:22 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:24 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:24 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:26 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:26 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:28 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:28 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:30 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:30 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:32 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:32 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:34 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:34 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:36 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:36 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:38 WARN ReceiverSupervisorImpl: Restarting receiver with delay 2000 ms: Error connecting to localhost:9998
java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:38 ERROR ReceiverTracker: Deregistered receiver for stream 0: Restarting receiver with delay 2000ms: Error connecting to localhost:9998 - java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.Net.connect0(Native Method)
        at java.base/sun.nio.ch.Net.connect(Net.java:591)
        at java.base/sun.nio.ch.Net.connect(Net.java:580)
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:593)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633)
        at java.base/java.net.Socket.connect(Socket.java:583)
        at java.base/java.net.Socket.<init>(Socket.java:507)
        at java.base/java.net.Socket.<init>(Socket.java:287)
        at org.apache.spark.streaming.dstream.SocketReceiver.onStart(SocketInputDStream.scala:61)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.startReceiver(ReceiverSupervisor.scala:150)
        at org.apache.spark.streaming.receiver.ReceiverSupervisor.$anonfun$restartReceiver$1(ReceiverSupervisor.scala:201)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
        at java.base/java.lang.Thread.run(Thread.java:840)
26/09/29 08:55:39 WARN ReceiverSupervisorImpl: Receiver has been stopped
26/09/29 08:55:40 WARN BatchedWriteAheadLog: BatchedWriteAheadLog Writer queue interrupted.
26/09/29 08:55:45 WARN ReceivedBlockTracker: Exception thrown while writing record: BatchAllocationEvent(1790672145000 ms,AllocatedBlocks(Map(0 -> List()))) to the WriteAheadLog.
java.lang.IllegalStateException: close() was called on BatchedWriteAheadLog before write request with time 1790672145002 could be fulfilled.
        at org.apache.spark.streaming.util.BatchedWriteAheadLog.write(BatchedWriteAheadLog.scala:89)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.writeToLog(ReceivedBlockTracker.scala:251)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.allocateBlocksToBatch(ReceivedBlockTracker.scala:125)
        at org.apache.spark.streaming.scheduler.ReceiverTracker.allocateBlocksToBatch(ReceiverTracker.scala:211)
        at org.apache.spark.streaming.scheduler.JobGenerator.$anonfun$generateJobs$1(JobGenerator.scala:256)
        at scala.util.Try$.apply(Try.scala:217)
        at org.apache.spark.streaming.scheduler.JobGenerator.generateJobs(JobGenerator.scala:255)
        at org.apache.spark.streaming.scheduler.JobGenerator.org$apache$spark$streaming$scheduler$JobGenerator$$processEvent(JobGenerator.scala:188)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:92)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:91)
        at org.apache.spark.util.EventLoop$$anon$1.run(EventLoop.scala:50)
26/09/29 08:55:45 WARN ReceivedBlockTracker: Exception thrown while writing record: BatchCleanupEvent(List(1790672120000 ms)) to the WriteAheadLog.
java.lang.IllegalStateException: close() was called on BatchedWriteAheadLog before write request with time 1790672145264 could be fulfilled.
        at org.apache.spark.streaming.util.BatchedWriteAheadLog.write(BatchedWriteAheadLog.scala:89)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.writeToLog(ReceivedBlockTracker.scala:251)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.cleanupOldBatches(ReceivedBlockTracker.scala:183)
        at org.apache.spark.streaming.scheduler.ReceiverTracker.cleanupOldBlocksAndBatches(ReceiverTracker.scala:231)
        at org.apache.spark.streaming.scheduler.JobGenerator.clearCheckpointData(JobGenerator.scala:295)
        at org.apache.spark.streaming.scheduler.JobGenerator.org$apache$spark$streaming$scheduler$JobGenerator$$processEvent(JobGenerator.scala:192)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:92)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:91)
        at org.apache.spark.util.EventLoop$$anon$1.run(EventLoop.scala:50)
26/09/29 08:55:45 WARN ReceivedBlockTracker: Failed to acknowledge batch clean up in the Write Ahead Log.
26/09/29 08:55:50 WARN ReceivedBlockTracker: Exception thrown while writing record: BatchAllocationEvent(1790672150000 ms,AllocatedBlocks(Map(0 -> List()))) to the WriteAheadLog.
java.lang.IllegalStateException: close() was called on BatchedWriteAheadLog before write request with time 1790672150001 could be fulfilled.
        at org.apache.spark.streaming.util.BatchedWriteAheadLog.write(BatchedWriteAheadLog.scala:89)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.writeToLog(ReceivedBlockTracker.scala:251)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.allocateBlocksToBatch(ReceivedBlockTracker.scala:125)
        at org.apache.spark.streaming.scheduler.ReceiverTracker.allocateBlocksToBatch(ReceiverTracker.scala:211)
        at org.apache.spark.streaming.scheduler.JobGenerator.$anonfun$generateJobs$1(JobGenerator.scala:256)
        at scala.util.Try$.apply(Try.scala:217)
        at org.apache.spark.streaming.scheduler.JobGenerator.generateJobs(JobGenerator.scala:255)
        at org.apache.spark.streaming.scheduler.JobGenerator.org$apache$spark$streaming$scheduler$JobGenerator$$processEvent(JobGenerator.scala:188)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:92)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:91)
        at org.apache.spark.util.EventLoop$$anon$1.run(EventLoop.scala:50)
26/09/29 08:55:50 WARN ReceivedBlockTracker: Exception thrown while writing record: BatchCleanupEvent(List(1790672125000 ms, 1790672120000 ms)) to the WriteAheadLog.
java.lang.IllegalStateException: close() was called on BatchedWriteAheadLog before write request with time 1790672150094 could be fulfilled.
        at org.apache.spark.streaming.util.BatchedWriteAheadLog.write(BatchedWriteAheadLog.scala:89)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.writeToLog(ReceivedBlockTracker.scala:251)
        at org.apache.spark.streaming.scheduler.ReceivedBlockTracker.cleanupOldBatches(ReceivedBlockTracker.scala:183)
        at org.apache.spark.streaming.scheduler.ReceiverTracker.cleanupOldBlocksAndBatches(ReceiverTracker.scala:231)
        at org.apache.spark.streaming.scheduler.JobGenerator.clearCheckpointData(JobGenerator.scala:295)
        at org.apache.spark.streaming.scheduler.JobGenerator.org$apache$spark$streaming$scheduler$JobGenerator$$processEvent(JobGenerator.scala:192)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:92)
        at org.apache.spark.streaming.scheduler.JobGenerator$$anon$1.onReceive(JobGenerator.scala:91)
        at org.apache.spark.util.EventLoop$$anon$1.run(EventLoop.scala:50)
26/09/29 08:55:50 WARN ReceivedBlockTracker: Failed to acknowledge batch clean up in the Write Ahead Log.
Streaming accumulator contribution: 0
[success] Total time: 49 s, completed Sep 29, 2026, 8:55:50 AM
vishnupriya@vishnupriya:~/scala-spark-30-day-practice/Day-30-Final-Educational-Capstone$
```
