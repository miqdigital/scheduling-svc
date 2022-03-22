Environment

| Environment | Type | Job Link | Description |
|:-------------|:------|:----------|:-------------|
| PR | 0 click | [Link](https://builds-corp.mediaiqdigital.com/view/platform/job/platform-scheduling-service/) |This Job gets triggered automatically whenever a pull request is raised and deploys pull request code in  PR environment  and run Integration tests to check the Code stability. |
| QA | 0 click | [Link](https://builds-corp.mediaiqdigital.com/view/platform/job/platform-scheduling-service-nightly-builds/) | This Job Builds every nights and deploys latest development code in QA Environment and runs integration tests to check the Code Stability. |
| PRE-PROD | 0 click | [Link](https://builds-corp.mediaiqdigital.com/view/platform/job/platform-scheduling-service-release-pipeline/) | PRE-PROD Environment comes up as a part of release start and we run integration tests on this environment , PRE-PROD Environment comes down as a part of release finish. |
| PRODUCTION | 1 click | [Link](https://builds-corp.mediaiqdigital.com/view/platform/job/platform-scheduling-service-deploy/) | Product Deployment is One click deployment , Latest release image will be deployed in to this environment as soon as we do  release finish. |
| INTEGRATION | 1 click | [Link](https://builds-corp.mediaiqdigital.com/view/platform/job/platform-scheduling-service-deploy/) | One click deployment , Usually Production and Integration Environment has same version deployed. |

