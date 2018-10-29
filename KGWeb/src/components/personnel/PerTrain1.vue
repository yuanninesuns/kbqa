<template>
     <div >
       <h1 v-show="false">{{graph}}</h1>
     </div>
</template>
<script>
import d3 from "d3";
export default {
  name: "qiuqiu",
  props:{
    graph:{
      type:Object,
      required:true
    }
  },
  data() {
    return {
      p:true
    };
  },
  mounted() {
    //页面加载
    // alert("mounted")
  },
  beforeUpdate(){
    // alert("beforeUpdate")
  },
  updated(){
    // alert("uuu");
    const width = 900,
      height = 500;
    const color = d3.scale.category20();//颜色数目 后面的20 是个变数

    const radius = d3.scale.sqrt().range([0, 6]);//球球半径
    d3.select('svg').remove();   //删除整个SVG
    // d3.select('svg')
    //   .selectAll('*')
    //   .remove(); 
    
    const svg = d3
      .select(this.$el)
      .append("svg")
      .attr("width", width)
      .attr("height", height);

    

    const force = d3.layout
      .force()
      .size([width, height])
      .charge(-400)//小球的分散程度
      .linkDistance(function(d) {
        return radius(d.source.size) + radius(d.target.size) + 60;
      });

    force
      .nodes(this.graph.nodes)
      .links(this.graph.links)
      .on("tick", tick)
      .start();

    const link = svg
      .selectAll(".link")
      .data(this.graph.links)
      .enter()
      .append("g")
      .attr("class", "link");

    link.append("line").style("stroke-width", function(d) {
      return (d.bond * 2 - 1) * 2 + "px";
    });

    link
      .filter(function(d) {
        return d.bond > 1;
      })
      .append("line")
      .attr("class", "separator");

    var node = svg
      .selectAll(".node")
      .data(this.graph.nodes)
      .enter()
      .append("g")
      .attr("class", "node")
      .call(force.drag);

    node
      .append("circle")
      .attr("r", function(d) {
        return radius(d.size);
      })
      .style("fill", function(d) {
        return color(d.atom);
      });

    node
      .append("text")
      .attr("dy", ".35em")
      .attr("text-anchor", "middle")
      .text(function(d) {
        return d.atom;
      });
    function tick() {
      link
        .selectAll("line")
        .attr("x1", function(d) {
          return d.source.x;
        })
        .attr("y1", function(d) {
          return d.source.y;
        })
        .attr("x2", function(d) {
          return d.target.x;
        })
        .attr("y2", function(d) {
          return d.target.y;
        });

      node.attr("transform", function(d) {
        return "translate(" + d.x + "," + d.y + ")";
      });
    }
  },
  beforeDestroyed(){
    // alert(" beforeDestroyed")
  },
  methods: {}
};
</script>
<style >
.link line {
  stroke: #696969;
}

.link line.separator {
  stroke: #fff;
  stroke-width: 2px;
}

.node circle {
  stroke: #000;
  stroke-width: 1.5px;
}

.node text {
  font: 10px sans-serif;
  pointer-events: none;
}
</style>