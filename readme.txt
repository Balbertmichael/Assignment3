Assumptions:
Assumed that word ladder if it exists will be bounded by 150 rungs.
I implemented multiple depth bounds for dfs as the runtime for each iteration is relatively short.
If that isn't the case then simply raise the HIGHEST_BOUND to a higher number.

Testing:
For testing, I just built a word generator that would take a random word from dict and test to see if bfs and dfs could find a word ladder. BFS has been consistent with word ladders so basically testing was mostly on dfs.

Note:
For dfs, I implemented a bfs like solution that basically makes the two methods equal in word ladder length though it has a runtime trade off.