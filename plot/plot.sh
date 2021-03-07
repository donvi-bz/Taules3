# Using Placeholders:
uLoc = "D:/Laptop/Java/Taules3/plot/data.txt"
u1a = 1
u1b = 2

# Variables
rangeMax = 0
rangeMin = 7 * 24
lineW = 2

# Settings
set terminal wxt size 1600,900
set xrange [0:24]
set yrange [-1:200]

set xlabel "Time (hours)"
set ylabel "Messages sent"

set xtics 1
set key left top box

#/!

plot \
  uLoc using 0:1 \
  title "Sunday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:2 \
  title "Monday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:3 \
  title "Tuesday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:4 \
  title "Wednesday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:5 \
  title "Thursday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:6 \
  title "Friday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:7 \
  title "Saturday"\
  with lines \
  linewidth lineW \
  , \
  uLoc using 0:8 \
  title "Average"\
  with lines \
  linewidth lineW*2