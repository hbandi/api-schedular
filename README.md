# api-schedular
API - Scheduler(Under Development)

Sometimes you need to schedule API calls/Service calls to do some activity.This will be done typically using cron expressions or quartz schedulers,if you have very few in number it will be easy to maintain using crons/schedulars.

I tried to write simple API scheduler with little configurations.

For example,We took simple use case like .
On every 10 mins we need to call service endpoint with given context.For that configuration will be like this 

```
{
  "tasks": [
    {
      "id": "customer-alpha",
      "context": "alpha",
      "schedule": "0 0/10 * * * *",
      "endPoint": "http://localhost:8090/alpha/call",
      "successCallBackEndPoint": "http://localhost:8090/alpha/success",
      "failureCallBackEndPoint": "http://localhost:8090/alpha/failure"
    }
  ]
}
```
In task configuration,
we created task for service alpha, Id as “customer-alpha” and context is “alpha” ,with these two fields we identify unique task.

On every 10 min we call given REST API endpoint with context,after calling we wait configured time in millis and then
if we get response code 200 ,we treat API call is success and then call success end point.
If we get !200 ,we will call failure end point.

That’s it we are completed processing this configured task.On every 10 min we repeat this process.

How It works.
We have following REST endpoints exposed from this service 

### POST /api/1.0/task
  Caller can register task with api-scheduler service,it will give response success/failure/duplicate.
### PUT /api/1.0/task
  Caller can upsert task,it will give success/failure
### DELETE /api/1.0/task
  Caller Can delete task with api-scheduler
### GET /api/1.0/task
Caller can query task details 






 


