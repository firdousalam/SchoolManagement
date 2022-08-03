import { isEmpty } from 'lodash';
export const applicationObjectCreation = (applicationObject: any, userId: any, applicationState: string, tempDocument?: any)=>{  
    return {
         application:{
             applicationState: applicationState,
             approvalStatus: applicationState,
             approverRemark: "",
             batchId: 1                       
         },        
         docList: [   
            ...applicationObject.docList         
           ],
         docTempList: [       
            ...tempDocument     
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
             approvalStatus: applicationState
         },
         stdEducationList:[
             ...applicationObject.stdEducationList
         ],
         stdProfession:{     
            ...applicationObject.stdProfessionList,
            organization: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.organization:'',             
            designation: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.designation:'',              
            placeOfDuty: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.placeOfDuty:''            
         },       
         userId: userId
    }
 }
 export const applicationObjectUpdate = (applicationObject: any, userId: any, applicationState: string, tempDocument?: any)=>{  
    console.log("applicationObject",applicationObject);
    const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
    let profileId = studentProfileObj.studentProfileId;
    
    return {
        application:{
            applicationState: applicationState,
            approvalStatus: applicationState,
            approverRemark: "",
            batchId: 1,
            dateOfEntry: new Date(),
            id: 0,
            profileId: applicationObject.id,
            status: 0,
            updatedAt: new Date(),
            updatedBy: ''                       
        },        
        docList: [   
        ...applicationObject.docList         
        ],
        docTempList: [       
        ...tempDocument     
        ],        
        stdAdmission: {            
            applicationCategory: "",
            approvalStatus: "",
            batchNo: "",
            certificateLink: "",
            courseCenter: "",
            createdAt: "",
            createdBy: "",            
            enrollmentNumber: "",
            id: 0,
            idCardNo: "",
            profileId: 0,
            status: 0,
            updatedAt: "",
            updatedBy: "",
            validityTill: ""
        },  
        stdContactDetail:{
            ...applicationObject.stdContactDetail,   
            approvalStatus:""        
        },  
        stdPersonalDetail:{
            ...applicationObject.personalDetails,
            dateofEntry:"",
            approvalStatus: applicationState
        },
        stdEducationList:[
            ...applicationObject.stdEducationList
        ],
        stdProfession:{     
        ...applicationObject.stdProfessionList,
        organization: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.organization:'',             
        designation: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.designation:'',              
        placeOfDuty: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.placeOfDuty:''            
        },       
        userId: userId,
        id: (applicationObject.id)?applicationObject.id:0,        
        status: 0,
        docMetaDataList: applicationObject.docMetaDataList
    }
 }
 