<template>
  <svg width="1280" height="800">
    <g style="transform: translate(0, 100px)">
      <path :d="line" />
    </g>
  </svg>
</template>
<script>
// import * as d3 from 'd3';
import * as d3_js from './d3.js';
import * as d3_layout from './d3.layout.js';
import * as d3_geom from './d3.geom.js';

// window._ = require('d3.layout.js')
export default {
  name: 'vue-line-chart',
  data() {
    return {
      data: [99, 71, 78, 25, 36, 92],
      line: '',
    };
  },
  mounted() {
    alert("!!!");
    this.init_gragh();
    // this.update();
  },
  methods: {
    getScales() {
      const x = d3.scaleTime().range([0, 430]);
      const y = d3.scaleLinear().range([210, 0]);
      d3.axisLeft().scale(x);
      d3.axisBottom().scale(y);
      x.domain(d3.extent(this.data, (d, i) => i));
      y.domain([0, d3.max(this.data, d => d)]);
      return { x, y };
    },
    calculatePath() {
      const scale = this.getScales();
      const path = d3.line()
        .x((d, i) => scale.x(i))
        .y(d => scale.y(d));
      this.line = path(this.data);
    },
    init_gragh(){
      var w = 1280,
      h = 800,
      node,
      link,
      root;

    var force = d3_layout.superfunc.d3.layout.force()
      .on("tick", tick)
      .charge(function (d) { return d._children ? -d.size / 100 : -30; })
      .linkDistance(function (d) { return d.target._children ? 80 : 30; })
      .size([w, h - 160])

    var vis = d3_js.superfunc.select("body").append("svg:svg")
      .attr("width", w)
      .attr("height", h)

    d3.json("http://mbostock.github.io/d3/talk/20111116/flare.json", function (json) {
      root = json;
      root.fixed = true;
      root.x = w / 2;
      root.y = h / 2 - 80;
      update();
    });
    function update() {
      var nodes = flatten(root),
        links = d3_layout.superfunc.d3.layout.tree().links(nodes);

      // Restart the force layout.
      force
        .nodes(nodes)
        .links(links)
        .start();

      // Update the links¡­
      link = vis.selectAll("line.link")
        .data(links, function (d) { return d.target.id; });

      // Enter any new links.
      link.enter().insert("svg:line", ".node")
        .attr("class", "link")
        .attr("x1", function (d) { return d.source.x; })
        .attr("y1", function (d) { return d.source.y; })
        .attr("x2", function (d) { return d.target.x; })
        .attr("y2", function (d) { return d.target.y; });

      // Exit any old links.
      link.exit().remove();

      // Update the nodes¡­
      node = vis.selectAll("circle.node")
        .data(nodes, function (d) { return d.id; })
        .style("fill", color);

      node.transition()
        .attr("r", function (d) { return d.children ? 4.5 : Math.sqrt(d.size) / 10; });

      // Enter any new nodes.
      node.enter().append("svg:circle")
        .attr("class", "node")
        .attr("cx", function (d) { return d.x; })
        .attr("cy", function (d) { return d.y; })
        .attr("r", function (d) { return d.children ? 4.5 : Math.sqrt(d.size) / 10; })
        .style("fill", color)
        .on("click", click)
        .call(force.drag);

      // Exit any old nodes.
      node.exit().remove();
    }

    function tick() {
      link.attr("x1", function (d) { return d.source.x; })
        .attr("y1", function (d) { return d.source.y; })
        .attr("x2", function (d) { return d.target.x; })
        .attr("y2", function (d) { return d.target.y; });

      node.attr("cx", function (d) { return d.x; })
        .attr("cy", function (d) { return d.y; });
    }

    // Color leaf nodes orange, and packages white or blue.
    function color(d) {
      return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
    }

    // Toggle children on click.
    function click(d) {
      if (d.children) {
        d._children = d.children;
        d.children = null;
      } else {
        d.children = d._children;
        d._children = null;
      }
      update();
    }

    // Returns a list of all nodes under the root.
    function flatten(root) {
      var nodes = [], i = 0;

      function recurse(node) {
        if (node.children) node.size = node.children.reduce(function (p, v) { return p + recurse(v); }, 0);
        if (!node.id) node.id = ++i;
        nodes.push(node);
        return node.size;
      }

      root.size = recurse(root);
      return nodes;
    }
    }
  },
};

</script>
<style lang="sass" scoped>
svg{margin: 25px;}
  
path{fill: none
  stroke: #76BF8A
  stroke-width: 3px}

// body {
//   font: 300 36px "Helvetica Neue";
//   height: 640px;
//   margin: 80px 160px 80px 160px;
//   overflow: hidden;
//   position: relative;
//   width: 960px;
// }

// a:link, a:visited {
//   color: #777;
//   text-decoration: none;
// }

// a:hover {
//   color: #666;
// }

// blockquote {
//   margin: 0;
// }

// blockquote:before {
//   content: "“";
//   position: absolute;
//   left: -.4em;
// }

// blockquote:after {
//   content: "”";
//   position: absolute;
// }

// body > ul {
//   margin: 0;
//   padding: 0;
// }

// h1 {
//   font-size: 64px;
// }

// h1, h2, h3 {
//   font-weight: inherit;
//   margin: 0;
// }

// h2, h3 {
//   text-align: right;
//   font-size: inherit;
//   position: absolute;
//   bottom: 0;
//   right: 0;
// }

// h2 {
//   font-size: 24px;
//   position: absolute;
// }

// h3 {
//   bottom: -20px;
//   font-size: 18px;
// }

// .invert {
//   background: #1f1f1f;
//   color: #dcdccc;
// }

// .invert h2, .invert h3 {
//   color: #7f9f7f;
// }

// .string, .regexp {
//   color: #f39;
// }

// .keyword {
//   color: #00c;
// }

// .comment {
//   color: #777;
//   font-style: oblique;
// }

// .number {
//   color: #369;
// }

// .class, .special {
//   color: #1181B8;
// }

// body > svg {
//   position: absolute;
//   top: -80px;
//   left: -160px;
// }

</style>
