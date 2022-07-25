export const applicationObjectCreation = (applicationObject: any, userId: any, applicationState: string)=>{   

    return {
         application:{
             applicationState: applicationState,
             approvalStatus: applicationState,
             approverRemark: "",
             batchId: 1                       
         },        
         docList: [            
           ],
         docTempList: [            
         ],        
         stdAdmission: {            
             applicationCategory: "",           
             batchNo: "",
             certificateLink: "",
             courseCenter: "",
             enrollmentNumber: "",            
             idCardNo: "",            
             validityTill: "",
             dateofEntry:"",
             id: null
           },  
         stdContactDetail:{
             ...applicationObject.stdContactDetail,   
             approvalStatus:""        
         },  
         stdPersonalDetail:{
             ...applicationObject.personalDetails,
             dateofEntry:"",
             employmentStatus:"Employed",
             approvalStatus: applicationState
         },
         stdEducationList:[
             ...applicationObject.stdEducationList
         ],
         stdProfession:{
             ...applicationObject.stdProfessionList            
         },       
         userId: userId
    }
 }
 