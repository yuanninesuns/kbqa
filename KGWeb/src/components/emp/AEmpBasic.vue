<template>
  <div class="table">
    <div class="search-Box">
      <el-input   placeholder="请输入关键字" icon="search"  class="search"  v-model="search"></el-input>
    </div>
    <el-table  :data="tables" border style="width: 100%">
      <el-table-column  prop="date" label="日期"></el-table-column>
      <el-table-column prop="name" label="姓名"> </el-table-column>
      <el-table-column   prop="address"  label="地址"></el-table-column>
    </el-table>
  </div>
</template>
<script>
  export default{
    data(){
      return {
        search: '',  //搜索
        tableData: [
          { date: '2016-05-02',name: '王小虎', address: '上海市普陀区金沙江路 1518 弄' },
          {date: '2016-05-04',name: '章小虎',address: '上海市普陀区金沙江路 1517 弄'},
          {date: '2016-05-01',name: '李小虎',address: '上海市普陀区金沙江路 1519 弄'},
        ]  //表格内容
      }
    },
    computed:{
      tables:function(){
        var search=this.search;
        if(search){
          return  this.tableData.filter(function(dataNews){
            return Object.keys(dataNews).some(function(key){
              return String(dataNews[key]).toLowerCase().indexOf(search) > -1
            })
          })
        }
        return this.tableData
      }
    }
  }
</script>