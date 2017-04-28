var flag = 0;
class SearchLeaveApplication extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],employ:{},searchSet:{

    name:"",
    code:"",
    departmentId:"",
    designationId:"",employeeTypeId:""},isSearchClicked:false,assignments_department:[],assignments_designation:[],employeeType:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.handleBlur=this.handleBlur.bind(this);

  }




  search(e)
  {
      e.preventDefault();
    let {
    name,
    code,
    departmentId,
    designationId,
    employeeTypeId}=this.state.searchSet;
    e.preventDefault();
    //call api call
    var employees=[];
    employees=commonApiPost("hr-employee","employees","_search",{tenantId,code,departmentId,designationId,pageSize:500},this.state.searchSet).responseJSON["Employee"] || [];
    flag = 1;
    this.setState({
      isSearchClicked:true,
      employees,searchSet:{

      name:"",
      code:"",
      departmentId:"",
      designationId:"",
      employeeType:""}
    });
  }


  componentWillMount()
  {

    this.setState({
      assignments_department,
      assignments_designation,
      employeeType
  })
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#employeeTable').dataTable().fnDestroy();
    }
  }

  handleChange(e,name)
  {

      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  handleBlur(e) {
    setTimeout(function(){
       if(document.activeElement.id == "sub") {
          $("#sub").click();
       }
    }, 100);
    var _this=this;

    if(e.target.value) {
      try{
        var code = e.target.value;
       //Make get employee call
       var obj = commonApiPost("hr-employee","employees","_search",{code,tenantId}).responseJSON["Employee"][0];
       console.log(obj);
       _this.setState({
         searchSet:{
             ..._this.state.searchSet,
             name:obj.name
           }
     })
  }
      catch (e){
        console.log(e);
      }
  } else {
    _this.setState({
      searchSet: {
        ..._this.state.searchSet,
        name: ""
      }
    })
  }
}




  close(){
      // widow.close();
      open(location, '_self').close();
  }


  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.employees.length!=this.state.employees.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.employees.length);
          // alert(this.state.employees.length);
          // alert('updated');
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,
             language: {
              "emptyTable": "No Records"
            }
          });
      }
  }

  render() {
    let {handleChange,search,handleBlur}=this;
    let {isSearchClicked,employees}=this.state;
    let {name,
    code,
    departmentId,
    designationId,employeeTypeId}=this.state.searchSet;
    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }


    const renderAction=function(type,id){
      if (type==="create") {

              return (
                      <a href={`app/hr/leavemaster/compensetory-leave.html?id=${id}&type=${type}`}>Apply Compensetory Leave</a>
              );

    }else {
            return (
                    <a href={`app/hr/leavemaster/view-compensetory.html?id=${id}&type=${type}`}>View</a>
            );
        }
}


    const showTable=function()
    {
      if(isSearchClicked)
      {
          return (
            <table id="employeeTable" className="table table-bordered">
                <thead>
                    <tr>
                        <th>Sl No</th>
                        <th>Worked Date</th>
                        <th>Employee Name</th>
                        <th>Employee Code</th>
                        <th>Status</th>
                        <th>master</th>

                    </tr>
                </thead>

                <tbody id="employeeSearchResultTableBody">
                    {
                        renderBody()
                    }
                </tbody>

            </table>

          )


      }

    }
    const renderBody=function()
    {
      if(employees.length>0)
      {
      return employees.map((item,index,id)=>
      {
            return (<tr key={index}>
                    <td>{index+1}</td>
                    <td data-label="workedDate">{item.workedDate}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="code">{item.code}</td>
                    <td data-label="status">{item.status}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>

                </tr>
            );
      })
    } 
  }

    return (
      <div>
        <h3>Search employee to {getUrlVars()["type"]} a Compensetory application</h3>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
          <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Designation  </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select id="designationId" name="designationId" value={designationId} onChange={(e)=>{
                                handleChange(e,"designationId")}}>
                            <option value= "">Select Designation</option>
                            {renderOption(this.state.assignments_designation)}
                           </select>
                        </div>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Department  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="departmentId" name="departmentId" value={departmentId}
                              onChange={(e)=>{ handleChange(e,"departmentId")}}>
                                <option  value= "">Select Department</option>
                                {renderOption(this.state.assignments_department)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
                </div>


              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code}
                              onChange={(e)=>{handleChange(e,"code")}} onBlur={(e)=>{handleBlur(e)}}/>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" id="name" name="name" value={name}
                                onChange={(e)=>{  handleChange(e,"name")}} disabled/>
                          </div>
                      </div>
                    </div>
            </div>

                {/*<div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for=""> Status</label>
                              </div>
                              <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="employeeStatusCode" name="employeeStatusCode" value={employeeStatusCode}
                                    onChange={(e)=>{ handleChange(e,"employeeStatusCode") }}>

                                      <option value="">Select Status</option>
                                      {renderOption(this.state.employeeStatus)}
                                 </select>
                              </div>
                              </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                  <label for="">Functionary  </label>
                                </div>
                                <div className="col-sm-6">
                                <div className="styled-select">
                                    <select id="functionaryCode" name="functionaryCode" value={functionaryCode}
                                      onChange={(e)=>{  handleChange(e,"functionaryCode")}}>

                                    <option>Select Functionary</option>
                                    {renderOption(this.state.assignments_functionary)}
                                   </select>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>

              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Type  </label>
                          </div>
                          <div className="col-sm-6">
                            <div className="styled-select">
                              <select id="employeeTypeId" name="employeeTypeId" value={employeeTypeId} onChange={(e)=>{
                                  handleChange(e,"employeeTypeId")}}>
                              <option>Select Type</option>
                              {renderOption(this.state.employeeType)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
                </div>*/}


            <div className="text-center">
                <button id="sub" type="submit"  className="btn btn-submit">Search</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
            </div>
          </fieldset>
          </form>
          <br/>
          {showTable()}
      </div>
    );
  }
}






ReactDOM.render(
  <SearchLeaveApplication />,
  document.getElementById('root')
);
