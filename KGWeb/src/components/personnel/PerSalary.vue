<template>
<el-container>
  <el-header style="width = 100%;padding: 0px;display:flex;">
    <p style="font-size:20px;"><b>管理员审核</b></p>
  </el-header>
  <el-container>
    <el-header style="display:flex;padding: 0px">
      <el-form :inline="true">
        <el-form-item label="选择审核类型" Opacity="0.50">
          <el-select v-model="formInline.region" placeholder="">
            <el-option label="节点" value="node"></el-option>
            <el-option label="关系" value="relation"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-header>
    <br>
    <div v-show="formInline.region==='node'">
      <!--  -->
      <el-table  :data="tableData" style="text-align:left;">
        <el-table-column prop="date" label="类型" width="140">
        </el-table-column>
        <el-table-column prop="name" label="名称" width="120">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="handleEdit(scope.$index, scope.row)">添加</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--  -->
    </div>
    <div v-show="formInline.region==='relation'">
      <el-table  :data="tableData2" style="text-align:left;">
        <el-table-column prop="date" label="类型1" width="140">
        </el-table-column>
        <el-table-column prop="name" label="名称1" width="120">
        </el-table-column>
        <el-table-column prop="date2" label="类型2"  width="140">
        </el-table-column>
        <el-table-column prop="name2" label="名称2" width="120">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope1">
            <el-button size="mini" type="success" @click="handleEdit2(scope1.$index, scope1.row)">添加</el-button>
            <el-button size="mini" type="danger" @click="handleDelete2(scope1.$index, scope1.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
  </el-container>
  
</el-container>
</template>

<script>
// import repeat from "./repeat.vue"

export default {
  tableData: [],
  data() {
    const item = {
        date: '2016-05-02',
        name: '王小虎'
      };
    types: [
      { name: "症状", num: 0 },
      { name: "药品", num: 1 },
      { name: "科室", num: 2 },
      { name: "疾病", num: 3 },
      { name: "部位", num: 4 },
      { name: "检查", num: 5 }
    ];
    return {
      formInline: {
        user: "",
        region: ""
      },  
      tableData: [],
      tableData2:[],
    };
  },
  mounted(){
    this.tableData = [];
    this.tableData2 = [];
    this.postRequest("/employee/basic/importEmp", {}).then(resp => {
        resp.data.forEach(element => {
          var temp = element.split(" ");
          this.tableData.push({date:temp[0],name:temp[1]});
        });
      }); 
    this.postRequest("/employee/basic/importEmp2", {}).then(resp => {
        resp.data.forEach(element => {
          var temp = element.split(" ");
          this.tableData2.push({date:temp[0],name:temp[1],date2:temp[2],name2:temp[3]});
        });
      }); 
    // this.tableData.push({date: '2016-05-02',name: '王小虎'},{date: '2016-05-03',name: '王小虎'},{date: '2016-05-04',name: '王小虎'});
    // this.tableData2.push({date: '2016-05-02',name: '王小虎',date2:"2015",name2:'汪小涛'},{date: '2016-05-03',name: '王小虎',date2:"2015",name2:'汪小涛'})
  },
  destroyed(){
    // alert("destroyed");
    var array = "";
    this.tableData.forEach(elements =>{
      array += elements.date+" "+elements.name +"@";
    })
    // alert(array)
    this.postRequest("/employee/basic/updateFile", {
        arr: array
      }).then(resp => {
      //  alert("108")
      }); 
    
    var array2 = "";
    this.tableData2.forEach(elements =>{
      array2 += elements.date+" "+elements.name +" "+elements.date2+" "+elements.name2 +"@";
    })
    alert(array2)
    this.postRequest("/employee/basic/updateRFile", {
        arr: array2
      }).then(resp => {
      //  alert("124")
      }); 
  },
  methods: {
    showNode(){
      // alert("节点")
    },
    handleEdit(index, row) {
      this.postRequest("/employee/basic/addNewNode", {
        node: row.date,
        value: row.name
      }).then(resp => {
       alert(resp.data)
      }); 
      this.tableData.splice(this.tableData.indexOf(row),1);
        // console.log(index, row);
    },
    handleDelete(index, row) {
      alert("您选中的index是"+index+", row是"+row+"!");
      this.tableData.splice(this.tableData.indexOf(row),1);
    },
    handleEdit2(index, row) {
      // alert("您选中的index是"+index+", row是"+row.date+","+row.name+","+row.date2+","+row.name2+"!");
      this.postRequest("/employee/basic/addNewRelation", {
        node: row.date,
        value: row.name,
        node2: row.date2,
        value2: row.name2
      }).then(resp => {
       alert(resp.data)
      }); 
      this.tableData2.splice(this.tableData.indexOf(row),1);
    },
    handleDelete2(index, row) {
      alert("您选中的index是"+index+", row是"+row+"!");
      // console.log(index, row);
      this.tableData2.splice(this.tableData2.indexOf(row),1);
    }
  },
};
</script>