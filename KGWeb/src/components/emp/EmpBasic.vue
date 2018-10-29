<template>
  <div class="note" :style ="note">
  <div>
    <el-container>
      <el-header style="width = 100%;padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <div style="display: inline " >
          <el-input
            placeholder="输入你想要问的问题,记得回车哦..."
            clearable
            @change="keywordsChange"
            style="width: 500px;margin: 0px;padding: 0px;"
            size="mini"
            :disabled="advanceSearchViewVisible"
            @keyup.enter.native="loadEmps"
            prefix-icon="el-icon-search"
            v-model="keywords">
          </el-input>
          <el-button type="primary" size="mini" style="margin-left: 5px" icon="el-icon-search" @click="loadEmps">搜索
          </el-button>
        </div> 
      </el-header>

      <br>
      <br>
      <div  style="width = 50%">
          <!-- <p style="width = 100%;padding: 0px;display:flex;justify-content:space-between;align-items: center">结果</p>
          <span style="width = 100%;padding: 0px;display:flex;justify-content:space-between;align-items: center">{{myData[0].name}}</span> -->
          <el-table :data="myData" border  style="text-align:left;width: 60%" >
            <el-table-column prop="name" label="结果"></el-table-column>
          </el-table>
      </div>
      
    </el-container>
    <el-container style="width :60%;text-align:left;display:flex;">
      <el-header style="padding:0">
        <p font-size="100px"><b>相似问题</b></p>
      </el-header>
      <el-main style="padding: 0px;">
          <el-collapse  v-model="activeNames" @change="handleChange">
            <el-collapse-item v-for="sentence in sentences" :key="sentence" v-bind:title="sentence.title" v-bind:name="sentence.name">
              <div><p v-html="sentence.sent"></p></div>
            </el-collapse-item>
          </el-collapse>
      </el-main>
    </el-container>
  </div> 
  </div>
</template>

<script>

export default {
    name: 'productdetailspage',
    data() {
        return {
            myData:[{name:"暂时没有结果"}],
            keywords: '',
            activeNames: ['1'],
            // sentences:[{title:"一致性 Consistency",sent:"与现实生活一：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；与现实生活一：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；与现实生活一：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；",name:"1"},
            //     {title:"反馈 Feedback",sent:"控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；",name:"2"},
            //     {title:"效率 Efficiency",sent:"简化流程：设计简洁直观的操作流程；",name:"3"},
            //     {title:"可控 Controllability",sent:"用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；",name:"4"},
            // ],
            sentences:[]
        }
    },
    methods: {
      loadEmps(){
        var _this = this;
        // alert("70");
        this.postRequest('/employee/basic/basicdata',{
          str: this.keywords
        }).then(resp=>{  
          this.myData = [];
              resp.data.forEach(element => {
                // alert(element);
                this.myData.push({"name":element});
              });
        }),
        this.postRequest('/employee/basic/getSimilarity',{
          str: this.keywords
        }).then(resp=>{  
          this.sentences = []
          var i = 1
          alert(resp.data)
          resp.data.forEach(element => {
          
          var temp = element.split("&");
          this.sentences.push({title:temp[0],sent:temp[1],name:i});
          i++;
        });  
        })
      },
      handleChange(val) {
        // alert(val);
      }
    }
}
</script>
<style>

</style>
