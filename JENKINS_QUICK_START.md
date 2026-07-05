# Quick Start: Jenkins Pipeline for Travel Booking API

## 🚀 5-Minute Setup

### Prerequisites
- Jenkins 2.387+ installed
- Java 17+
- Maven 3.8+

### Step 1: Install Plugins (2 minutes)

Go to **Manage Jenkins** → **Manage Plugins** → **Available**

Search and install:
1. Pipeline
2. GitHub
3. Git
4. JUnit

Click **Install without restart**

### Step 2: Add GitHub Credentials (1 minute)

1. **Manage Jenkins** → **Manage Credentials** → **System** → **Global credentials**
2. **Add Credentials**
   - Kind: Username with password
   - Username: `abhipnm`
   - Password: [Your GitHub PAT]
   - ID: `github-credentials`

### Step 3: Create Pipeline Job (2 minutes)

1. **New Item**
2. Name: `travel-booking-api`
3. Type: **Pipeline**
4. **OK**

### Step 4: Configure Pipeline

**Pipeline** section:
- Definition: **Pipeline script from SCM**
- SCM: **Git**
- Repo: `https://github.com/abhipnm/travel-booking-api.git`
- Credentials: `github-credentials`
- Branch: `*/main`
- Script Path: `Jenkinsfile`

**Build Triggers** (Optional):
✓ GitHub hook trigger for GITScm polling

Click **Save**

### Step 5: First Build

1. Click **Build Now**
2. Watch console output
3. See all **10 stages** execute

---

## 📊 Pipeline Stages

```
Stage 1: 🔍 Checkout Code → Clones repository
Stage 2: 📋 Code Quality Analysis → Analyzes code
Stage 3: 🏗️ Build Project → Maven build
Stage 4: 🧪 Unit Tests → 9 unit tests
Stage 5: 🔗 Integration Tests → 13 integration tests
Stage 6: 📊 Test Report → Publishes results
Stage 7: 📦 Artifact Management → Archives JAR
Stage 8: 🔒 Security Scan → Security checks
Stage 9: 📤 Deploy to Staging → Deployment ready
Stage 10: ✅ Post Build → Summary
```

---

## 🧪 22 Test Cases (Automatic)

### Unit Tests (9)
- Create, read, update, delete bookings
- Filter by email and status
- Error handling

### Integration Tests (13)
- API endpoints
- Validation errors
- Status transitions
- Full CRUD operations

---

## 🎯 What Happens on Push

1. Developer pushes code to main branch
2. GitHub webhook triggers Jenkins
3. Jenkins clones repository
4. Runs **22 automated tests**
5. Builds application
6. Generates reports
7. Archives artifact
8. Ready for deployment

---

## 📝 Console Output Example

```
[Pipeline] stage
[Pipeline] { (🔍 Checkout Code)
[Pipeline] checkout
 * Getting changes from https://github.com/abhipnm/travel-booking-api.git
 * branch main -> FETCH_HEAD
 * Checking out Revision bf8ea5c
[Pipeline] }
[Pipeline] stage
[Pipeline] { (🏗️ Build Project)
[Pipeline] sh
 * Running 'mvn clean package -DskipTests -q'
 * BUILD SUCCESS [00:15]
[Pipeline] }
[Pipeline] stage
[Pipeline] { (🧪 Unit Tests)
[Pipeline] sh
 * Running unit tests...
 * Tests run: 9, Failures: 0, Skipped: 0
 * BUILD SUCCESS [00:08]
[Pipeline] }
[Pipeline] stage
[Pipeline] { (🔗 Integration Tests)
[Pipeline] sh
 * Running integration tests...
 * Tests run: 13, Failures: 0, Skipped: 0
 * BUILD SUCCESS [00:12]
[Pipeline] }
[Pipeline] stage
[Pipeline] { (✅ Post Build)
 * ✅ PIPELINE SUCCESS
 * Build ID: #1
 * All tests passed
 * Ready for production
[Pipeline] }
```

---

## 🔧 Environment Variables

The pipeline uses:
```groovy
APP_NAME = "travel-booking-api"
VERSION = Build number
ARTIFACT_NAME = travel-booking-api-${BUILD_NUMBER}.jar
GIT_CREDENTIALS = github-credentials
```

---

## 🌐 Configure GitHub Webhook (Optional)

For **automatic** builds on push:

1. GitHub repo → **Settings** → **Webhooks**
2. **Add webhook**
   - Payload URL: `http://your-jenkins-url/github-webhook/`
   - Content type: `application/json`
   - Events: **Just the push event**
   - Active: ✓
3. **Add webhook**

Now every push triggers a build automatically!

---

## 📊 View Results

### During Build
- Jenkins Dashboard → `travel-booking-api` → Last Build

### After Build
- **Console Output**: Full build logs
- **Test Results**: Pass/fail counts
- **Artifacts**: Download JAR files

### Email Notifications (Optional)
Configure post-build actions to send results via email

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| Build fails at checkout | Verify GitHub credentials |
| Tests fail | Run locally: `mvn test` |
| Webhook not triggering | Check GitHub webhook delivery logs |
| Build timeout | Increase timeout in Jenkinsfile |

---

## 📚 Next Steps

1. ✅ Run first pipeline build
2. ✅ Configure GitHub webhook
3. ✅ Set up email notifications
4. ✅ Add code coverage reporting
5. ✅ Configure production deployment
6. ✅ Add branch protection rules

---

## 🎓 Advanced Configuration

### Add Code Coverage
```groovy
stage('📈 Code Coverage') {
    steps {
        sh 'mvn jacoco:report'
    }
}
```

### Deploy to Docker
```groovy
stage('🐳 Docker Build') {
    steps {
        sh 'docker build -t travel-booking-api:${BUILD_NUMBER} .'
    }
}
```

### Deploy to Production
```groovy
stage('🚀 Deploy to Production') {
    when { branch 'main' }
    steps {
        sh 'kubectl apply -f k8s/deployment.yaml'
    }
}
```

---

## 📞 Support

- Jenkins Docs: https://www.jenkins.io/doc/
- Pipeline Syntax: https://www.jenkins.io/doc/book/pipeline/
- GitHub Integration: https://www.jenkins.io/doc/book/pipeline/repo-examples/

---

**You're all set! Your CI/CD pipeline is ready!** 🎉
