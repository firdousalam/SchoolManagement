export const apiRoutes = {
  alertsApi: {
    create: '/api/v1/alerts/createAlerts',
    getAll: '/api/v1/alerts/getAllAlerts',
    search: '/api/v1/alerts/getAlertsBySearchCriteria',
    getReferenceId: '/api/v1/alerts/getReferenceId',
    update: '/api/v1/alerts/updateAlerts',
  },

  batchApi: {
    create: '/api/v1/batch/createBatch',
    getAll: '/api/v1/batch/getAllBatch',
    search: '/api/v1/batch/getBatchBySearchCriteria',
    getReferenceId: '/api/v1/batch/getReferenceId',
    update: '/api/v1/batch/updateBatch',
  },

  batchEventApi: {
    create: '/api/v1/batchEvent/createBatchEvent',
    getAll: '/api/v1/batchEvent/getAllBatchEvents',
    search: '/api/v1/batchEvent/getBatchEventBySearchCriteria',
    getReferenceId: '/api/v1/batchEvent/getReferenceId',
    update: '/api/v1/batchEvent/updateBatchEvent',
    save: '/api/v1/batchEvent/saveBatchEvents',
  },

  batchAlertApi: {
    create: '/api/v1/batchAlerts/createBatchAlert',
    getAll: '/api/v1/batchAlerts/getAllBatchAlerts',
    search: '/api/v1/batchAlerts/getBatchAlertsBySearchCriteria',
    getReferenceId: '/api/v1/batchAlerts/getReferenceId',
    update: '/api/v1/batchAlerts/updateBatchAlert',
  },

  configParamApi: {
    create: '/api/v1/configParam/createConfigParam',
    getAll: '/api/v1/configParam/getAllConfigParam',
    search: '/api/v1/configParam/getConfigParamBySearchCriteria',
    getReferenceId: '/api/v1/configParam/getReferenceId',
    update: '/api/v1/configParam/updateConfigParam',
  },

  configValueApi: {
    create: '/api/v1/configValue/createConfigValue',
    getAll: '/api/v1/configValue/getAllConfigValue',
    search: '/api/v1/configValue/getConfigValueBySearchCriteria',
    getReferenceId: '/api/v1/configValue/getReferenceId',
    update: '/api/v1/configValue/updateConfigValue',
  },

  configMapApi: {
    create: '/api/v1/configMap/createConfigMap',
    getAll: '/api/v1/configMap/getAllConfigMap',
    search: '/api/v1/configMap/getConfigMapBySearchCriteria',
    getReferenceId: '/api/v1/configMap/getReferenceId',
    update: '/api/v1/configMap/updateConfigMap',
    save: '/api/v1/configMap/saveConfigMaps',
  },

  courseCalendarApi: {
    create: '/api/v1/courseCalendar/createCourseCalendar',
    getAll: '/api/v1/courseCalendar/getAllCourseCalendar',
    search: '/api/v1/courseCalendar/getCourseCalendarBySearchCriteria',
    getReferenceId: '/api/v1/courseCalendar/getReferenceId',
    update: '/api/v1/courseCalendar/updateCourseCalendar',
  },

  courseDocumentApi: {
    create: '/api/v1/courseDocument/createCourseDocument',
    getAll: '/api/v1/courseDocument/getAllCourseDocument',
    search: '/api/v1/courseDocument/getCourseDocumentBySearchCriteria',
    getReferenceId: '/api/v1/courseDocument/getReferenceId',
    update: '/api/v1/courseDocument/updateCourseDocument',
  },

  dataApi: {
    create: '/api/v1/data/createData',
    getAll: '/api/v1/data/getAllData',
    search: '/api/v1/data/getDataBySearchCriteria',
    getReferenceId: '/api/v1/data/getReferenceId',
    update: '/api/v1/data/updateData',
  },

  eventsApi: {
    create: '/api/v1/events/createEvent',
    getAll: '/api/v1/events/getAllEvents',
    search: '/api/v1/events/getEventsBySearchCriteria',
    getReferenceId: '/api/v1/events/getReferenceId',
    update: '/api/v1/events/updateEvent',
  },

  feesApi: {
    create: '/api/v1/fees/createFee',
    getAll: '/api/v1/fees/getAllFees',
    search: '/api/v1/fees/getFeesBySearchCriteria',
    getReferenceId: '/api/v1/fees/getReferenceId',
    update: '/api/v1/fees/updateFee',
  },

  institutionApi: {
    create: '/api/v1/institutions/createInstitution',
    getAll: '/api/v1/institutions/getAllInstitutions',
    search: '/api/v1/institutions/getInstitutionsBySearchCriteria',
    getReferenceId: '/api/v1/institutions/getReferenceId',
    update: '/api/v1/institutions/updateInstitution',
  },

  typeApi: {
    create: '/api/v1/type/createType',
    getAll: '/api/v1/type/getAllType',
    search: '/api/v1/type/getTypeBySearchCriteria',
    getReferenceId: '/api/v1/type/getReferenceId',
    update: '/api/v1/type/updateType',
  },
  queryOrContactApi: {
    create: '/api/v1/contactQuery/createContactQuery',
    getAll: '/api/v1/contactQuery/getAllContactQuery',
    getById: '/api/v1/contactQuery/getContactQueryById',
    search: '/api/v1/contactQuery/getContactQueryBySearchCriteria',
    getReferenceId: '/api/v1/contactQuery/getReferenceId',
    update: '/api/v1/contactQuery/updateContactQuery',
  },
  studyMaterialApi: {
    getAll: '/api/v1/studyMaterial/getStudyMaterialById',
    search: '/api/v1/studyMaterial/getStudyMaterialsBySearchCriteria',
    getDoc: '/api/v1/studyMaterial/documentData'
  },
  studentProfileApi: {
    create: '/api/v1/studentProfile/createStudentProfile',
    search: '/api/v1/studentProfile/getStudentProfileBySearchCriteria',
    update: '/api/v1/studentProfile/updateStudentProfile',
    getById: '/api/v1/studentProfile/getStdProfileById',
    getByIDWithDetail: '/api/v1/studentProfile/getStdProfileById',
    getByProfileByUserId: '/api/v1/studentProfile/getProfileByUserId'
  },

  studentContactDetailApi: {
    update: '/api/v1/studentContactDeatils/updateStudentContactDetails',
  },
  studentEducationApi: {
    update: '/api/v1/studentEducation/updateStudentEducation',
  },
  studentEmploymentApi: {
    update: '/api/v1/studentProfession/updateStudentProfession',
  },

  notificationApi: {
    search: '/api/v1/notifications/getNotificationsBySearchCriteria',
    getAll: '/api/v1/notifications/getAllNotifications',
  },
  admissionApi: {
    search: '/api/v1/admission/getAdmissionBySearchCriteria',
    update: '/api/v1/admission/updateAdmission'
  },
  paymentApi:{
    getById:'/api/v1/payment/getPaymentsByProfileId',
    createById: '/api/v1/payment/makePayment',
    upload: '/api/v1/studentDocument/upload'

  },
  batchDetailsApi:{
    getById: '/api/v1/studentProfile/getStdProfileListById',
  },
  studentPersonalApi:{
    update:'/api/v1/studentpersonalDetails/updateStudentPersonalDetail'
  }

};
