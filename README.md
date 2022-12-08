# Areavisibility

Visualize algorithm for generating a grid inside an area.

<img width="540" alt="Skjermbilde 2022-12-08 kl  22 56 38" src="https://user-images.githubusercontent.com/5525340/206575921-02eb9400-1629-4a28-8890-6080819f0368.png">

## Compile 

Use Maven to build `mvn install`


## Run

Run [Main](src/main/java/Main.java). To switch algorithm change the [main at line 35](https://github.com/t2gran/areavisibility/blob/3009355461ef26a8587953ec8802c505707beb19/src/main/java/Main.java#L35).

## Algorithms

### All visibility lines
This just add all visibility lines - this is very simple, but create to many lines and dificult to debug visually.  

 - [AllVLStrategy](src/main/java/geometri/vl/AllVLStrategy.java)


### Delaunay  lines

This strategy is similar to the [Delaunay triangulation](https://en.wikipedia.org/wiki/Delaunay_triangulation). 

When all visibility lines are computed, the algorithm sort them in increasing length order and add them one by one
if they not intersect with one of the existing added visibility lines.

- [DeluneyVLStrategy](src/main/java/geometri/vl/DeluneyVLStrategy.java)

#### Improvments

##### Before visibility lines generation

Not all points in an erea is connected to the rest of the graph. Only entrance points are connected. If the edge is 
convex between two points the points in between can be dropped. Note! This may have affect on other things, like 
linking in Stops etc. We can safly do this point pruning BEFORE we generate visibility lines.

![image](https://user-images.githubusercontent.com/5525340/206587661-3a8a87c0-93b6-4a22-8ed0-a4041dec45db.png)

##### After visibility lines generation

If we want to use the area graph in routing we can improve space and performance by removing all edges that is not part
of an optimal path. Route between all pairs of entrences in an area and mark all edges used. Then drop all vertexes and
edges not visited.



