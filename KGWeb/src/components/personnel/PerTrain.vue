<template>
    <el-container Opacity="0.5">
      <br>
      <p  style="text-align:left">添加节点</p>
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
        <el-form-item label="节点名" Opacity="0.50">
            <el-input v-model="formInline.user1" placeholder=""></el-input>
        </el-form-item>
        <el-form-item  Opacity="0.50">
            <el-button type="info" @click="submitNewNode()">提交<i class="el-icon-upload el-icon--right"></i></el-button>
        </el-form-item>
        </el-form>
      </el-header>
        <br>
        <p  style="text-align:left">添加关系</p>
        <br>
        <repeat></repeat>

    </el-container>
</template>

<script>
import repeat from "./repeat.vue"
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
    };
  },
  methods: {
    submitNewNode(){
      var kind = this.formInline.region;
      var node = this.formInline.user1;
      if(kind == ""){
        alert("请选择字段！");
        return;
      }
      if(node == ""){
        alert("请输入节点！");
        return;
      }
      alert(kind+" "+node);
      this.postRequest("/employee/basic/exportEmp", {
        kind: kind,
        node: node
      }).then(resp => {
        alert(resp.data);
      }); 
    }
  },
  components:{
      "repeat":repeat
  }
};
</script>
