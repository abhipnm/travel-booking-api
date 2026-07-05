# Jenkins Pipeline Setup Guide

## Overview

This project includes a complete Jenkins pipeline that automatically builds, tests, and deploys the application whenever code is pushed to GitHub.

## Pipeline Stages

The Jenkins pipeline consists of **10 visible stages**:

1. **🔍 Checkout Code** - Clones repository from GitHub
2. **📋 Code Quality Analysis** - Analyzes code quality
3. **🏗️ Build Project** - Compiles code with Maven
4. **🧪 Unit Tests** - Runs unit tests (BookingServiceTest)
5. **🔗 Integration Tests** - Runs integration tests (BookingControllerTest)
6. **📊 Test Report** - Generates and publishes test reports
7. **📦 Artifact Management** - Archives build artifacts
8. **🔒 Security Scan** - Performs security checks
9. **📤 Deploy to Staging** - Deploys to staging environment
10. **✅ Post Build** - Final verification and summary

---

## Prerequisites

### Jenkins Server
- Jenkins 2.387+ (latest LTS recommended)
- Java 17+
- Maven 3.8+

### Jenkins Plugins Required

Install these plugins in Jenkins:
1. **Pipeline** - For declarative pipeline support
2. **GitHub** - For GitHub integration
3. **Git** - For Git operations
4. **JUnit** - For test reporting
5. **Cobertura** - For code coverage (optional)
6. **SonarQube** - For code quality (optional)
7. **Docker** - For containerization (optional)
8. **Credentials** - For credential management

Install plugins via:
- Dashboard → Manage Jenkins → Manage Plugins
- Search and install each plugin

---

## Setup Instructions

### Step 1: Create GitHub Personal Access Token

1. Go to https://github.com/settings/tokens
2. Click "Generate new token" → "Generate new token (classic)"
3. Name: "jenkins-automation"
4. Select scopes:
   - `repo` (full control)
   - `admin:repo_hook` (for webhooks)
5. Click "Generate token" and copy it

### Step 2: Configure Jenkins Credentials

1. Jenkins Dashboard → Manage Jenkins → Manage Credentials
2. Click "System" → "Global credentials"
3. Click "Add Credentials"
4. Fill in:
   - Kind: Username with password
   - Username: `abhipnm`
   - Password: Paste your GitHub token
   - ID: `github-credentials`
   - Click "Create"

### Step 3: Create Jenkins Pipeline Job

1. Jenkins Dashboard → New Item
2. Enter job name: `travel-booking-api`
3. Select: **Pipeline**
4. Click **OK**

### Step 4: Configure Pipeline

In the job configuration:

#### Pipeline Section
- Definition: **Pipeline script from SCM**
- SCM: **Git**
- Repository URL: `https://github.com/abhipnm/travel-booking-api.git`
- Credentials: Select `github-credentials`
- Branch Specifier: `*/main`
- Script Path: `Jenkinsfile`

#### Build Triggers (Optional)
- Check: **GitHub hook trigger for GITScm polling**

Click **Save**

### Step 5: Configure GitHub Webhook (Optional - for automatic builds)

If you want builds to trigger automatically on push:

1. Go to GitHub repository settings
2. Webhooks → Add webhook
3. Payload URL: `http://your-jenkins-url/github-webhook/`
4. Content type: `application/json`
5. Events: Just the push event
6. Active: ✓ Checked
7. Click **Add webhook**

---

## Running the Pipeline

### Manual Trigger
1. Go to Jenkins Dashboard
2. Click on `travel-booking-api` job
3. Click **Build Now**
4. Watch the build progress

### Automatic Trigger (with webhook)
1. Push code to main branch: `git push origin main`
2. Jenkins automatically detects the push
3. Pipeline starts automatically

---

## Pipeline Output Example

```
========== PIPELINE EXECUTION =========="

Stage 1: 🔍 Checkout Code
  ✓ Code checked out successfully

Stage 2: 📋 Code Quality Analysis
  ✓ Code formatting check passed
  ✓ Dead code analysis completed

Stage 3: 🏗️ Build Project
  ✓ Build completed successfully

Stage 4: 🧪 Unit Tests
  ✓ Unit tests completed
  Tests run: 9
  Failures: 0
  Skipped: 0

Stage 5: 🔗 Integration Tests
  ✓ Integration tests completed
  Tests run: 13
  Failures: 0
  Skipped: 0

Stage 6: 📊 Test Report
  ✓ Test reports generated

Stage 7: 📦 Artifact Management
  ✓ Artifact ready for deployment
  travel-booking-api-1.jar (45MB)

Stage 8: 🔒 Security Scan
  ✓ No hardcoded secrets detected
  ✓ Dependency vulnerabilities checked

Stage 9: 📤 Deploy to Staging
  ✓ Ready for staging deployment

Stage 10: ✅ Post Build
  ✓ All stages completed successfully

========== PIPELINE SUCCESS ==========
Build ID: #1
All tests passed
Build successful
Artifact generated
Ready for production
```

---

## Test Coverage

### Unit Tests (9 tests)
Located in: `src/test/java/com/travelBooking/service/BookingServiceTest.java`

- Create booking
- Get booking by ID
- Booking not found
- Confirm booking
- Cancel booking
- Delete booking
- Filter by email
- Filter by status
- Update booking

### Integration Tests (13 tests)
Located in: `src/test/java/com/travelBooking/controller/BookingControllerTest.java`

- Health check
- Create booking success
- Invalid email validation
- Invalid number of people
- Get all bookings
- Get booking not found
- Confirm booking endpoint
- Cancel booking endpoint
- Delete booking endpoint
- Filter by email
- Filter by status

Total: **22 test cases**

---

## Pipeline Configuration Options

### Environment Variables

Edit `Jenkinsfile` to customize:

```groovy
environment {
    APP_NAME = "travel-booking-api"
    VERSION = "${BUILD_NUMBER}"
    ARTIFACT_NAME = "travel-booking-api-${BUILD_NUMBER}.jar"
    SONARQUBE_PROJECT = "travel-booking-api"
    GIT_CREDENTIALS = "github-credentials"
    DOCKER_REGISTRY = "docker.io"
}
```

### Build Parameters

Add parameters to job for:
- Environment selection (Dev, Staging, Prod)
- Test suite selection
- Deployment target
- Version override

---

## Advanced Configuration

### Adding Code Coverage

1. Add to `pom.xml`:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
</plugin>
```

2. Add stage to Jenkinsfile:
```groovy
stage('📈 Code Coverage') {
    steps {
        sh 'mvn jacoco:report'
    }
}
```

### Adding SonarQube Analysis

1. Configure SonarQube server in Jenkins
2. Add to Jenkinsfile:
```groovy
stage('🔬 SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube') {
            sh 'mvn sonar:sonar'
        }
    }
}
```

### Docker Image Build

Add to Jenkinsfile:
```groovy
stage('🐳 Docker Build') {
    steps {
        sh '''
            docker build -t ${DOCKER_REGISTRY}/travel-booking-api:${BUILD_NUMBER} .
            docker push ${DOCKER_REGISTRY}/travel-booking-api:${BUILD_NUMBER}
        '''
    }
}
```

---

## Troubleshooting

### Build Fails at Checkout
**Problem:** Git credentials not found
**Solution:** 
1. Verify credentials in Jenkins
2. Check GitHub token hasn't expired
3. Regenerate token if needed

### Tests Fail
**Problem:** Unit/Integration tests failing
**Solution:**
1. Check test logs in Jenkins console
2. Run tests locally: `mvn test`
3. Check database connectivity (for integration tests)

### Webhook Not Triggering
**Problem:** Jenkins not triggered on push
**Solution:**
1. Verify webhook URL is correct
2. Check GitHub webhook delivery logs
3. Ensure Jenkins is accessible from GitHub

### Build Timeout
**Problem:** Build takes too long
**Solution:**
1. Increase timeout: Change `timeout(time: 30)` in Jenkinsfile
2. Optimize Maven build (use `-T 1C` for parallel compilation)
3. Cache dependencies

---

## Monitoring & Logs

### Jenkins Console Output
- Shows all stage outputs
- Check for ✓ (success) or ✗ (failure)
- Timestamps for performance analysis

### Test Reports
- Jenkins Dashboard → Build → Test Result
- Shows pass/fail summary
- Links to individual test failures

### Artifacts
- Jenkins Dashboard → Build → Artifacts
- Download generated JAR files
- Useful for manual testing or deployment

---

## CI/CD Best Practices

1. **Run Tests Always**
   - Never skip tests in pipeline
   - Enforce minimum coverage threshold

2. **Fail Fast**
   - Stop pipeline on first failure
   - Don't waste resources on broken builds

3. **Clear Visibility**
   - Use descriptive stage names
   - Log important information
   - Generate reports

4. **Automated Deployment**
   - Deploy to staging after tests pass
   - Manual approval for production
   - Rollback plan ready

5. **Security**
   - Store secrets in Jenkins credentials
   - Scan dependencies for vulnerabilities
   - No hardcoded secrets in code

---

## Sample Jenkins Declarative Pipeline Commands

### Run Specific Tests
```bash
mvn test -Dtest=BookingServiceTest
mvn test -Dtest=BookingControllerTest
```

### Run All Tests
```bash
mvn test
```

### Build Without Tests
```bash
mvn clean package -DskipTests
```

### Run with Code Coverage
```bash
mvn clean test jacoco:report
```

### Run with SonarQube
```bash
mvn clean test sonar:sonar \
  -Dsonar.projectKey=travel-booking-api \
  -Dsonar.host.url=http://sonarqube:9000 \
  -Dsonar.login=your-token
```

---

## Next Steps

1. ✅ Set up Jenkins server
2. ✅ Install required plugins
3. ✅ Configure GitHub credentials
4. ✅ Create pipeline job
5. ✅ Configure GitHub webhook
6. ✅ Run first pipeline build
7. ✅ Monitor build results
8. ✅ Configure email notifications
9. ✅ Add code coverage reporting
10. ✅ Set up production deployment

---

## Support & Resources

- **Jenkins Documentation**: https://www.jenkins.io/doc/
- **Pipeline Syntax**: https://www.jenkins.io/doc/book/pipeline/syntax/
- **GitHub Integration**: https://www.jenkins.io/doc/book/pipeline/repo-examples/
- **Maven in Jenkins**: https://www.jenkins.io/doc/book/using/using-agents/

---

## Questions?

For detailed setup help, refer to:
- `README.md` - Project overview
- `API_DOCUMENTATION.md` - API reference
- Jenkinsfile - Pipeline configuration
