<template>
  <div class="ns">
    <div class="left_pad">
    <el-container Opacity="0.5">
      <br>
      <el-header style="width = 100%;padding: 0px;display:flex;justify-content:space-between">
        <el-form :inline="true" :model="formInline" Opacity="0.50" >
        <el-form-item label="提供字段" Opacity="0.50">
            <el-select v-model="formInline.region" placeholder="">
            <el-option label="症状" value="症状"></el-option>
            <el-option label="药品" value="药品"></el-option>
            <el-option label="科室" value="科室"></el-option>
            <el-option label="疾病" value="疾病"></el-option>
            <el-option label="治疗" value="治疗"></el-option>
            <el-option label="检查" value="检查"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="输入" Opacity="0.50">
            <el-input v-model="formInline.user" placeholder=""></el-input>
        </el-form-item>
        </el-form>
      </el-header>
        
      <el-row :gutter="10" >
        <el-col :span="8" v-for="se in select" :key="se.num" Opacity="0.50" >
          <div class="grid-content bg-purple">
            <div class="el_text">{{se.name}}</div>
            <el-button type="info" size = "mini" icon="el-icon-close" class = "el_set" @click="del(se.num)"></el-button>
          </div>
        </el-col>
      </el-row>

      <el-container style="height: 372px; border: 1px solid #eee">
        <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
          <el-menu :default-openeds="['1']">
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-menu"></i>导航</template>
              <el-menu-item-group  >
                <!-- v-for = "type in types" :key="type.num" -->
                <el-menu-item index="1-1" @click="getKeywords('症状')">症状</el-menu-item>
                <!-- <el-menu-item @click="getKeywords(type.name)">{{type.name}}</el-menu-item> -->
                <el-menu-item index="1-2" @click="getKeywords('药品')">药品</el-menu-item> 
                <el-menu-item index="1-3" @click="getKeywords('科室')">科室</el-menu-item>
                <el-menu-item index="1-4" @click="getKeywords('疾病')">疾病</el-menu-item>
                <el-menu-item index="1-5" @click="getKeywords('治疗')">治疗</el-menu-item>
                <el-menu-item index="1-6" @click="getKeywords('检查')">检查</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
  
        <el-container>
          
          <el-main>
            <!-- <el-table :data="myData" >
              
                <el-table-column prop="name" label="结果" @click="test(scope.$index,scope.row)" >
                  <template slot-scope="scope">
                  </template>
                </el-table-column>
            </el-table>
             -->
            <el-row v-for="dt in myData" :key="dt.name" Opacity="0.50" >
              <br>
              <span @click="hh(dt.name)" style="padding-top:20px;cursor:pointer;"><b>{{dt.name}}</b></span>
              <br>
            </el-row>
          </el-main>
        </el-container>
      </el-container>

      <br>
      <el-rate
        v-model="value3"
        show-text>
      </el-rate>
      
    </el-container>
  </div>
  <div class="right_pad">
    <p >医疗问题辅助视图</p> 
    <qiuqiu v-bind:graph="mygraph"></qiuqiu>
    <!-- v-bind后面的是自定义的属性 -->
    <!-- <p @click="flag1=!flag1">医疗问题辅助视图</p>  -->
  </div>
  </div> 
  
</template>

<script>
import PerTrain from './PerTrain1.vue'
import d3 from "d3";
export default {
  select: [],
  // mygraph:{},
  data() {
    types: [
      { name: "症状", num: 0 },
      { name: "药品", num: 1 },
      { name: "科室", num: 2 },
      { name: "疾病", num: 3 },
      { name: "治疗", num: 4 },
      { name: "检查", num: 5 }
    ];
    const item = {
      date: "2016-05-02",
      name: "王小虎",
      address: "上海市普陀区金沙江路 1518 弄"
    };
    
    return {
      flag1 : true,
      flag: 0,
      formInline: {
        user: "",
        region: ""
      },
      select: [],
      value3: null,
      options: [
        {
          value: "症状",
          label: "症状",
          children: [
            { value: "zhongyi", label: "中医症状" },
            { value: "xiyi", label: "西医症状" }
          ]
        },
        {
          value: "药品",
          label: "药品",
          children: [
            { value: "zhongyao", label: "中药" },
            { value: "xiyao", label: "西药" }
          ]
        },
        { value: "科室", label: "科室" },
        { value: "治疗", label: "治疗" },
        { value: "检查", label: "检查" },
        { value: "疾病", label: "疾病" }
      ],
      myData: [{ name: "暂时没有结果" }],
      mygraph: {
        nodes: [
          { atom: "医疗", size: 22 }, 
          { atom: "疾病", size: 22 },
          { atom: "药", size: 22 },
        ],
        links: [
          { source: 0, target: 1, bond: 1,},
          { source: 0, target: 2, bond: 1 },
          { source: 1, target: 2, bond: 1 },
        ]
      }
    };
  },
  methods: {
    test(index, row){
      alert(index+"!!!!"+row);
    },
    hh(dt){
      alert(dt);
      // alert(val);
      var i = 0;
      this.postRequest("/employee/basic/emp", {
        key: dt
        // key :"脱肛"//
      }).then(resp => {
        this.mygraph.nodes = [];
        this.mygraph.links = [];
        resp.data.forEach(element => {
          this.mygraph.nodes.push({ atom: element,size: 22 }); 
          this.mygraph.links.push({ source: 0, target: i, bond: 1});
          i++;
        });
      }); 
    },
    onSubmit() {
      console.log("submit!");
    },
    getKeywords(s) {
      var inputlabel = this.formInline.region;
      if (inputlabel == "") {
        alert("请选择字段！");
        return;
      }
      this.select.push({ name: inputlabel, num: this.flag });
      this.flag++;

      var inputcontent = this.formInline.user;
      if (inputcontent == "") {
        alert("请输入字段！");
        return;
      }
      this.select.push({ name: inputcontent, num: this.flag });
      this.flag++;

      this.select.push({ name: s, num: this.flag });
      this.flag++;
      // alert(inputlabel+" "+inputcontent+" "+s);
      var _this = this;
      this.postRequest("/employee/basic/maxWorkID", {
        s1: inputlabel,
        s2: inputcontent,
        s3: s
      }).then(resp => {
        this.myData = [];
        resp.data.forEach(element => {
          this.myData.push({ name: element });
        });
      });
    },
    del(number) {
      var temp = [];
      // alert(number)
      for (var i = this.select.length - 1; i >= 0; i--) {
        if (this.select[i].num == number) {
          this.select.pop();
          this.flag--;
          return;
        } else {
          temp.push(this.select[i]);
          this.select.pop();
          this.flag--;
        }
      }
    }
  },
  components:{
    "qiuqiu":PerTrain
  }
  
};
</script>

<style>
.ns {
  /* background: url(/src/components/save.jpg) no-repeat; */
  /* background:#990000; */
  background-size: 100%;
}
.left_pad{
  float: left;
  width: 50%;
  height: 100%;
}
.right_pad{
  float: left;
  width: 50%;
  height: 100%;
}
el-row {
  margin-bottom: 20px;
}
el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.bg-purple .el_text {
  float: left;
  width: 64%;
  height: 36px;
}
.bg-purple .el_set {
  display: block;
  /* position: absolute; */
  float: right;
  width: 36%;
  height: 36px;
  margin: 0;
  padding:0;
  right: 0;
  top: 0;
  /* background:url("../statistics/ds.png")no-repeat center top;
    background-size: 100%;
    background-position-y: absolute; */
  /* border-top-right-radius: 4px;
    border-bottom-right-radius: 4px; */
  border-radius: 4px;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
  line-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>

