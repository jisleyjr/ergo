-- count recorded common rows (v4 and v5)
select *, count_rows5 - count_rows4 as v5_v4_rows_diff
from (select count(*) as count_rows5 from applyTransactions),
     (select count(*) as count_rows4 from applyTransactions4),
     (select count(*) as common_rows
      from applyTransactions as t5
               join applyTransactions4 t4
                    on t5.blockId = t4.blockId);

-- group and count blocks by speedup range
select t4.time / t5.time as speedup, count(*) as num_blocks
from applyTransactions t5 join applyTransactions4 t4 on t5.blockId = t4.blockId
group by speedup
order by speedup;

-- count blocks with speedup >= 2
select sum(num_blocks) from (
select t4.time / t5.time as speedup, count(*) as num_blocks
from applyTransactions t5 join applyTransactions4 t4 on t5.blockId = t4.blockId
where speedup >= 2
group by speedup
order by speedup
)
;

-- count blocks and average speedup for each range
select t5.height / 100000                            as range,
       round(avg(t4.time * 100 / t5.time * 0.01), 2) as avg_speedup,
       count(*)                                      as num_blocks
from applyTransactions t5 join applyTransactions4 t4 on t5.blockId = t4.blockId
where t4.time / t5.time >= 1
group by range
order by range;


-- outliers which are slower in v5, can be the result of measuring fluctuations
select sum(num_blocks) from (
select t4.time * 10 / t5.time as ratio,
       count(*) as num_blocks
from applyTransactions t5 join applyTransactions4 t4 on t5.blockId = t4.blockId
where ratio < 10
  and t4.height > 400000 -- among resent blocks
group by ratio
order by ratio
)
;

-- blocks with least 1.2 speedup in v5
select sum(num_blocks) from (
select t5.height / 100000                            as range,
       round(avg(t4.time * 100 / t5.time * 0.01), 2) as avg_speedup,
       count(*) as num_blocks
from applyTransactions t5 join applyTransactions4 t4 on t5.blockId = t4.blockId
where t4.time * 100 / t5.time * 0.01 >= 1.2
group by range
order by range
)
;

-- find blocks where cost is less than time_us (v5)
select round(ratio * 0.1, 1) as ratio, count(*)
from (select height,
             tx_num,
             cost                as full_cost,
             time / 1000         as time_us,
             (time / 100) / cost as ratio
      from applyTransactions
      where time_us > full_cost)
group by ratio
;

-- find blocks where cost is less than time_us (v4)
select round(ratio * 0.1, 1) as ratio, count(*)
from (select height,
             tx_num,
             cost                as full_cost,
             time / 1000         as time_us,
             (time / 100) / cost as ratio
      from applyTransactions4
      where time_us > full_cost)
group by ratio
;

-- group and count blocks by cost/time ratio (v5)
select min(ratio), count(*), round(avg(tx_num), 2) as avg_tx_num
from (select height,
             tx_num,
             cost                 as full_cost,
             time / 1000          as time_us,
             cost / (time / 1000) as ratio
      from applyTransactions
      where time_us <= full_cost)
group by ratio / 10
;

-- group and count blocks by cost/time ratio (v4)
select min(ratio), count(*), round(avg(tx_num), 2) as avg_tx_num
from (select height,
             tx_num,
             cost                 as full_cost,
             time / 1000          as time_us,
             cost / (time / 1000) as ratio
      from applyTransactions4
      where time_us <= full_cost)
group by ratio / 10
;

-- group blocks by maxCost
select min(maxCost), count(*) from applyTransactions
group by maxCost / 1000000
;

-- group block by maxCost/cost ratio
select maxCost / cost as ratio, count(*), min(height) from applyTransactions
group by ratio
;

-- group blocks with small ratio
select round(maxCost * 10 / cost * 0.1, 1) as ratio, count(*), min(height) from applyTransactions
where ratio <= 3
group by ratio
;

-- smallest ratio block
select round(t5.maxCost * 10 / t5.cost * 0.1, 1) as ratio,
       t5.height, t5.tx_num, t5.maxCost,
       t5.cost as cost_v5,
       t5.time / 1000 as time_t5_us,
       7030268 / (t5.time / 1000) as scalability_v5,
       t4.cost as cost_v4,
       t4.time / 1000 as time_t4_us
from applyTransactions t5
         join applyTransactions4 t4 on t5.blockId = t4.blockId
where ratio < 2.6
;

-- group blocks with large ratio
select round(maxCost * 10000 / cost * 0.0001, 4) as ratio, count(*), min(height) from applyTransactions
where ratio >= 569
group by ratio
;

select round(maxCost * 10 / cost * 0.1, 1) as ratio,
       height, tx_num, maxCost, cost,
       time / 1000                         as time_us
from applyTransactions
where ratio >= 569
order by height desc
limit 20

;