# Using Placeholders:
uLoc = "D:/Laptop/Java/Taules3/plot/messageData_001.txt"

# Variables
lineW = 3

# Settings
set terminal wxt size 1600,900
set xrange [0:24]
set yrange [0:200]

set xlabel "Time of day (EST)" font ",13"
set ylabel "Messages per hour" font ",13"

set xtics 1
set ytics 10
set grid
set key left top box width 1 height 1

set title "Text Chat Activity"
set title font ",20" offset 0,-1

#/!

plot \
  uLoc using 1:2 \
  title "Sunday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:3 \
  title "Monday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:4 \
  title "Tuesday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:5 \
  title "Wednesday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:6 \
  title "Thursday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:7 \
  title "Friday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:8 \
  title "Saturday" \
  with lines \
  linewidth lineW \
  , \
  uLoc using 1:9 \
  title "Average" \
  with lines \
  linewidth lineW*2