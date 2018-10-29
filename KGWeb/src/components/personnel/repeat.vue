<template>
    <el-container >
      <el-header style="width = 100%;padding: 0px;display:flex;justify-content:space-between">
        <el-form :inline="true" :model="formInline1" Opacity="0.50" >
        <el-form-item label="提供字段" Opacity="0.50">
            <el-select v-model="formInline1.region" placeholder="">
            <el-option label="症状" value="症状"></el-option>
            <el-option label="药品" value="药品"></el-option>
            <el-option label="科室" value="科室"></el-option>
            <el-option label="疾病" value="疾病"></el-option>
            <el-option label="治疗" value="治疗"></el-option>
            <el-option label="检查" value="检查"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="节点名" Opacity="0.50">
            <el-input v-model="formInline1.user1" placeholder=""></el-input>
        </el-form-item>
        <!-- <el-form-item label="节点属性" Opacity="0.50">
            <el-input v-model="formInline.user2" placeholder=""></el-input>
        </el-form-item> &nbsp;&nbsp;&nbsp;
        <el-form-item  Opacity="0.50">
            <el-button type="info">提交<i class="el-icon-upload el-icon--right"></i></el-button>
        </el-form-item> -->
        
        </el-form>
      </el-header>  
        
      <el-form :inline="true" :model="formInline2" Opacity="0.50" style="display:flex;">
        <el-form-item label="提供字段" Opacity="0.50">
              <el-select v-model="formInline2.region" placeholder="">
              <el-option label="症状" value="症状"></el-option>
              <el-option label="药品" value="药品"></el-option>
              <el-option label="科室" value="科室"></el-option>
              <el-option label="疾病" value="疾病"></el-option>
              <el-option label="治疗" value="治疗"></el-option>
              <el-option label="检查" value="检查"></el-option>
              </el-select>
        </el-form-item>&nbsp;
        <el-form-item label="节点名" Opacity="0.50">
          <el-input v-model="formInline2.user1" placeholder=""></el-input>
        </el-form-item>
        <el-form-item  Opacity="0.50">
            <el-button type="info" @click="submitNewRelation()" >提交<i class="el-icon-upload el-icon--right"></i></el-button>
        </el-form-item> 
      </el-form>
      <br>
      <!-- 德玛西亚万岁 -->
      <!-- <el-form style="display:flex;padding-left:300px">
        
      </el-form> -->
    </el-container>
</template>


<script>
export default {
    name:"repeat",
  select: [],
  // mygraph:{},
  data() {
    types: [
      { name: "症状", num: 0 },
      { name: "药品", num: 1 },
      { name: "科室", num: 2 },
      { name: "疾病", num: 3 },
      { name: "部位", num: 4 },
      { name: "检查", num: 5 }
    ];
    
    return {
      flag1 : true,
      flag: 0,
      formInline1: {
        user: "",
        region: ""
      },
      formInline2: {
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
        { value: "疾病", label: "疾病" },
      ],
      myData: [{ name: "暂时没有结果" }],
    };
  },
  methods: {
    submitNewRelation(){
      var kind1 = this.formInline1.region;
      var node1 = this.formInline1.user1;
      var kind2 = this.formInline2.region;
      var node2 = this.formInline2.user1;
      if(kind2 == "" || node2 == "" || node1 == "" || kind2 == ""){
        alert("请选择/输入字段！");
        return;
      }
      alert(kind1+" "+node1);
      this.postRequest("/employee/basic/exportEmp2", {
        kind: kind1,
        node: node1,
        kind2: kind2,
        node2: node2
      }).then(resp => {
        alert(resp.data);
      }); 
    }
  }
};
</script>