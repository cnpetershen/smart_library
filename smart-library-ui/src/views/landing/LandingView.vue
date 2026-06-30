<template>
  <div class="landing-page">
    <!-- ========== HEADER ========== -->
    <header class="site-header" :class="{ 'header-scrolled': scrolled }">
      <div class="container header-inner">
        <div class="logo">
          <el-icon :size="28" color="#409EFF"><Reading /></el-icon>
          <span class="logo-text">智慧图书馆</span>
        </div>
        <nav class="main-nav">
          <a href="#features" @click.prevent="scrollTo('#features')">核心功能</a>
          <a href="#tech" @click.prevent="scrollTo('#tech')">技术架构</a>
          <a href="#faq" @click.prevent="scrollTo('#faq')">常见问题</a>
        </nav>
        <div class="header-actions">
          <el-button type="primary" size="large" @click="goLogin">登录系统</el-button>
        </div>
      </div>
    </header>

    <!-- ========== HERO ========== -->
    <section class="hero-section">
      <div class="container">
        <div class="hero-grid">
          <div class="hero-content">
            <h1 class="hero-title">智慧图书馆<br/>AI 数据分析系统</h1>
            <p class="hero-subtitle">基于规则引擎的智能洞察 + 纯软件零硬件依赖 + 30分钟本地部署</p>
            <div class="trust-badges">
              <div class="badge-item">
                <el-icon color="#409EFF"><CircleCheckFilled /></el-icon>
                <span>Spring Boot 3.2 企业级架构</span>
              </div>
              <div class="badge-item">
                <el-icon color="#409EFF"><CircleCheckFilled /></el-icon>
                <span>Vue 3 + Element Plus 现代界面</span>
              </div>
              <div class="badge-item">
                <el-icon color="#409EFF"><CircleCheckFilled /></el-icon>
                <span>内置 AI 数据分析（规则引擎）</span>
              </div>
              <div class="badge-item">
                <el-icon color="#409EFF"><CircleCheckFilled /></el-icon>
                <span>本地部署，数据自主可控</span>
              </div>
            </div>
            <div class="hero-cta">
              <el-button type="primary" size="large" round @click="goLogin">在线演示</el-button>
              <el-button size="large" round @click="scrollTo('#tech')">查看文档</el-button>
            </div>
          </div>
          <div class="hero-image">
            <div class="hero-screenshot">
              <div class="mock-dashboard">
                <div class="mock-header">
                  <div class="mock-dot" style="background:#ff5f57"></div>
                  <div class="mock-dot" style="background:#ffbd2e"></div>
                  <div class="mock-dot" style="background:#28c840"></div>
                  <span class="mock-title">📊 首页数据总览</span>
                </div>
                <div class="mock-body">
                  <div class="mock-stats">
                    <div class="mock-stat-card"><div class="mock-stat-num">1,280</div><div class="mock-stat-label">总藏书</div></div>
                    <div class="mock-stat-card"><div class="mock-stat-num">256</div><div class="mock-stat-label">总读者</div></div>
                    <div class="mock-stat-card"><div class="mock-stat-num">48</div><div class="mock-stat-label">今日借阅</div></div>
                    <div class="mock-stat-card"><div class="mock-stat-num">12</div><div class="mock-stat-label">待还图书</div></div>
                  </div>
                  <div class="mock-charts">
                    <div class="mock-chart" style="flex:2">
                      <div class="mock-chart-bar" style="height:40%"></div>
                      <div class="mock-chart-bar" style="height:65%"></div>
                      <div class="mock-chart-bar" style="height:50%"></div>
                      <div class="mock-chart-bar" style="height:80%"></div>
                      <div class="mock-chart-bar" style="height:55%"></div>
                      <div class="mock-chart-bar" style="height:70%"></div>
                      <div class="mock-chart-bar" style="height:45%"></div>
                    </div>
                    <div class="mock-chart" style="flex:1;border-left:1px solid #eee;padding-left:16px">
                      <div class="mock-pie"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="hero-wave">
        <svg viewBox="0 0 1440 120" preserveAspectRatio="none"><path d="M0,60 C360,120 720,0 1440,60 L1440,120 L0,120 Z" fill="#f8fafc"/></svg>
      </div>
    </section>

    <!-- ========== PAIN POINTS ========== -->
    <section class="pain-section">
      <div class="container">
        <h2 class="section-title">图书管理的日常，不该被这些琐事消耗</h2>
        <div class="pain-grid">
          <div class="pain-card">
            <div class="pain-icon">📊</div>
            <h3>数据只记不看</h3>
            <p class="pain-desc">借阅记录堆积在数据库，月底统计靠手工导出Excel</p>
            <div class="pain-compare">
              <div class="compare-before">
                <span class="compare-label">传统方式</span>
                <div class="compare-box excel-box">Excel 手工统计</div>
              </div>
              <el-icon><ArrowRight /></el-icon>
              <div class="compare-after">
                <span class="compare-label">系统方案</span>
                <div class="compare-box dashboard-box">首页仪表盘实时看板</div>
              </div>
            </div>
          </div>
          <div class="pain-card">
            <div class="pain-icon">📦</div>
            <h3>库存靠人脑记</h3>
            <p class="pain-desc">哪类书快借完了？哪些书没人碰？全凭印象</p>
            <div class="pain-compare">
              <div class="compare-before">
                <span class="compare-label">传统方式</span>
                <div class="compare-box excel-box">模糊记忆估算</div>
              </div>
              <el-icon><ArrowRight /></el-icon>
              <div class="compare-after">
                <span class="compare-label">系统方案</span>
                <div class="compare-box dashboard-box">库存状态+分类统计</div>
              </div>
            </div>
          </div>
          <div class="pain-card">
            <div class="pain-icon">⏰</div>
            <h3>逾期靠人工催</h3>
            <p class="pain-desc">翻借阅登记表、打电话、发邮件，效率低还容易漏</p>
            <div class="pain-compare">
              <div class="compare-before">
                <span class="compare-label">传统方式</span>
                <div class="compare-box excel-box">纸质登记表翻查</div>
              </div>
              <el-icon><ArrowRight /></el-icon>
              <div class="compare-after">
                <span class="compare-label">系统方案</span>
                <div class="compare-box dashboard-box">逾期自动提醒列表</div>
              </div>
            </div>
          </div>
          <div class="pain-card">
            <div class="pain-icon">🎯</div>
            <h3>采购凭经验猜</h3>
            <p class="pain-desc">新书上架后借阅量如何？该不该多买科技类？没有数据支撑</p>
            <div class="pain-compare">
              <div class="compare-before">
                <span class="compare-label">传统方式</span>
                <div class="compare-box excel-box">凭经验盲目采购</div>
              </div>
              <el-icon><ArrowRight /></el-icon>
              <div class="compare-after">
                <span class="compare-label">系统方案</span>
                <div class="compare-box dashboard-box">AI趋势分析辅助决策</div>
              </div>
            </div>
          </div>
        </div>
        <div class="section-cta">
          <el-button type="primary" size="large" round @click="scrollTo('#features')">看看系统如何解决</el-button>
        </div>
      </div>
    </section>

    <!-- ========== CORE FEATURES ========== -->
    <section id="features" class="features-section">
      <div class="container">
        <h2 class="section-title">一套后台，覆盖图书管理全链路</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">📚</div>
            <h3>图书全生命周期管理</h3>
            <p class="feature-desc">从入库到下架，支持批量操作与智能检索，库存与借阅状态实时联动</p>
            <div class="feature-tags">
              <el-tag size="small">CRUD</el-tag>
              <el-tag size="small" type="success">ISBN校验</el-tag>
              <el-tag size="small" type="warning">分类标签</el-tag>
              <el-tag size="small" type="info">封面上传</el-tag>
              <el-tag size="small" type="danger">库存联动</el-tag>
            </div>
            <div class="feature-mock">
              <div class="mock-table">
                <div class="mock-table-row header"><span>书名</span><span>分类</span><span>库存</span><span>状态</span></div>
                <div class="mock-table-row"><span>深入理解Java虚拟机</span><span><span class="mock-tag t1">计算机</span></span><span>3/5</span><span><span class="mock-status available">可借</span></span></div>
                <div class="mock-table-row"><span>三体</span><span><span class="mock-tag t2">文学</span></span><span>0/2</span><span><span class="mock-status empty">已空</span></span></div>
                <div class="mock-table-row"><span>算法导论</span><span><span class="mock-tag t1">计算机</span></span><span>2/3</span><span><span class="mock-status available">可借</span></span></div>
              </div>
            </div>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔄</div>
            <h3>借阅归还一站式办理</h3>
            <p class="feature-desc">办理借阅自动扣减库存，归还时自动计算逾期天数，乐观锁防并发超借</p>
            <div class="feature-tags">
              <el-tag size="small">借阅办理</el-tag>
              <el-tag size="small" type="success">库存校验</el-tag>
              <el-tag size="small" type="warning">逾期计算</el-tag>
              <el-tag size="small" type="danger">乐观锁</el-tag>
            </div>
            <div class="feature-mock">
              <div class="mock-tabs">
                <span class="mock-tab active">📖 借阅办理</span>
                <span class="mock-tab">↩️ 归还办理</span>
              </div>
              <div class="mock-form">
                <div class="mock-input"><label>读者ID</label><span>R2024001</span></div>
                <div class="mock-input"><label>图书ID</label><span>B2024001</span></div>
                <div class="mock-btn">办理借阅</div>
              </div>
            </div>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📈</div>
            <h3>AI 借阅趋势分析</h3>
            <p class="feature-desc">规则引擎自动识别借阅高峰与低谷，生成可执行的运营建议</p>
            <div class="feature-tags">
              <el-tag size="small">近30天折线</el-tag>
              <el-tag size="small" type="success">峰谷识别</el-tag>
              <el-tag size="small" type="warning">周环比</el-tag>
              <el-tag size="small" type="info">运营建议</el-tag>
            </div>
            <div class="feature-ai-badge">
              <el-tag type="warning" effect="dark" size="small">基于规则引擎，非大模型API，离线可用</el-tag>
            </div>
            <div class="feature-mock">
              <div class="mock-line-chart">
                <div class="mock-line"></div>
                <div class="mock-peak-label">📈 高峰: 3月15日 (42次)</div>
                <div class="mock-peak-label valley">📉 低谷: 3月19日 (8次)</div>
              </div>
              <div class="mock-ai-card">
                <el-icon color="#E6A23C"><WarningFilled /></el-icon>
                <span>建议：周末借阅量高于工作日32%，建议增加周末工作人员</span>
              </div>
            </div>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🎯</div>
            <h3>智能图书推荐</h3>
            <p class="feature-desc">基于借阅历史的双策略推荐：同类热门 + 相似读者行为，每条推荐附带可信度</p>
            <div class="feature-tags">
              <el-tag size="small">热门TOP10</el-tag>
              <el-tag size="small" type="success">内容推荐</el-tag>
              <el-tag size="small" type="warning">协同过滤</el-tag>
              <el-tag size="small" type="info">可信度</el-tag>
            </div>
            <div class="feature-mock">
              <div class="mock-rec-tabs">
                <span class="mock-rec-tab active">🔥 热门TOP10</span>
                <span class="mock-rec-tab">👤 专属推荐</span>
              </div>
              <div class="mock-rec-list">
                <div class="mock-rec-item"><span>1. 三体</span><span class="mock-confidence"><span class="mock-bar" style="width:95%"></span>95%</span></div>
                <div class="mock-rec-item"><span>2. 深入理解Java虚拟机</span><span class="mock-confidence"><span class="mock-bar" style="width:88%"></span>88%</span></div>
                <div class="mock-rec-item"><span>3. 算法导论</span><span class="mock-confidence"><span class="mock-bar" style="width:82%"></span>82%</span></div>
              </div>
            </div>
          </div>
          <div class="feature-card feature-card-wide">
            <div class="feature-icon">📄</div>
            <h3>一键数据报告</h3>
            <p class="feature-desc">月度运营报告一键生成，Markdown格式直接导出，汇报无需手动整理</p>
            <div class="feature-tags">
              <el-tag size="small">月度报告</el-tag>
              <el-tag size="small" type="success">借阅总量</el-tag>
              <el-tag size="small" type="warning">库存缺口</el-tag>
              <el-tag size="small" type="info">阅读习惯分析</el-tag>
            </div>
            <div class="feature-mock report-mock">
              <div class="mock-report-btn">📄 生成月度运营报告</div>
              <div class="mock-report-preview">
                <div class="mock-report-title"># 2026年5月图书馆运营报告</div>
                <div class="mock-report-line">- 本月借阅总量：**156** 次</div>
                <div class="mock-report-line">- 日均借阅：**5.2** 次</div>
                <div class="mock-report-line">- 最热门分类：计算机科学 (32%)</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== TECH ARCHITECTURE ========== -->
    <section id="tech" class="tech-section">
      <div class="container">
        <h2 class="section-title">开源技术栈，透明可审计</h2>
        <div class="tech-arch">
          <div class="arch-flow">
            <div class="arch-node"><span class="arch-label">Vue 3</span><small>Element Plus</small></div>
            <el-icon class="arch-arrow"><ArrowRight /></el-icon>
            <div class="arch-node"><span class="arch-label">Axios</span><small>HTTP</small></div>
            <el-icon class="arch-arrow"><ArrowRight /></el-icon>
            <div class="arch-node"><span class="arch-label">Spring Boot 3.2</span><small>RESTful API</small></div>
            <el-icon class="arch-arrow"><ArrowRight /></el-icon>
            <div class="arch-node"><span class="arch-label">MyBatis-Plus</span><small>ORM</small></div>
            <el-icon class="arch-arrow"><ArrowRight /></el-icon>
            <div class="arch-node db-node"><span class="arch-label">MySQL 8.0</span><small>+ Redis</small></div>
          </div>
          <div class="tech-tags">
            <el-tag>JWT认证</el-tag>
            <el-tag type="success">BCrypt加密</el-tag>
            <el-tag type="warning">乐观锁</el-tag>
            <el-tag type="info">逻辑删除</el-tag>
            <el-tag>本地文件存储</el-tag>
            <el-tag type="success">RESTful API</el-tag>
            <el-tag type="warning">Element Plus</el-tag>
          </div>
        </div>
        <div class="tech-compare">
          <h3 class="compare-title">对比传统方案与AI概念产品</h3>
          <div class="compare-table-wrapper">
            <table class="compare-table">
              <thead>
                <tr><th>维度</th><th class="highlight">本系统</th><th>部分传统系统</th><th>AI概念产品</th></tr>
              </thead>
              <tbody>
                <tr><td>AI实现</td><td class="highlight">规则引擎，零API成本</td><td>无AI或仅统计报表</td><td>大模型API，有额外成本</td></tr>
                <tr><td>部署方式</td><td class="highlight">本地部署，数据自主</td><td>本地部署，架构老旧</td><td>SaaS云端，数据外迁</td></tr>
                <tr><td>硬件依赖</td><td class="highlight">纯软件，零硬件</td><td>需RFID/门禁对接</td><td>需硬件或云端绑定</td></tr>
                <tr><td>前端技术</td><td class="highlight">Vue 3 + Element Plus</td><td>JSP/老旧框架</td><td>混合技术栈</td></tr>
                <tr><td>响应速度</td><td class="highlight">毫秒级（本地+Redis缓存）</td><td>秒级</td><td>依赖API网络延迟</td></tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="section-cta">
          <el-button size="large" round @click="scrollTo('#faq')">查看技术文档</el-button>
        </div>
      </div>
    </section>

    <!-- ========== USE CASES ========== -->
    <section class="usecase-section">
      <div class="container">
        <h2 class="section-title">为这类场景精准设计</h2>
        <div class="usecase-grid">
          <div class="usecase-card">
            <div class="usecase-icon">🏫</div>
            <h3>高校/中学图书馆</h3>
            <p>班级图书角、院系资料室、校图书馆，双角色区分管理员与读者</p>
          </div>
          <div class="usecase-card">
            <div class="usecase-icon">🏢</div>
            <h3>企业/单位图书室</h3>
            <p>员工自助借阅，HR后台统一管理，借阅记录自动归档</p>
          </div>
          <div class="usecase-card">
            <div class="usecase-icon">📖</div>
            <h3>社区/小型公共馆</h3>
            <p>轻量化部署，无需专业运维，一台服务器即可运行</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== DEMO & EXPERIENCE ========== -->
    <section class="demo-section">
      <div class="container">
        <h2 class="section-title">先看效果，再决定</h2>
        <div class="demo-content">
          <div class="demo-info">
            <div class="demo-accounts">
              <h3>测试账号</h3>
              <div class="account-item">
                <el-tag type="primary">管理员</el-tag>
                <code>admin / admin123</code>
              </div>
              <div class="account-item">
                <el-tag type="success">读者</el-tag>
                <code>reader1 / 123456</code>
              </div>
            </div>
            <div class="demo-flow">
              <h3>体验动线</h3>
              <div class="flow-steps">
                <div class="flow-step"><span class="step-num">1</span>首页仪表盘</div>
                <el-icon><ArrowRight /></el-icon>
                <div class="flow-step"><span class="step-num">2</span>图书管理</div>
                <el-icon><ArrowRight /></el-icon>
                <div class="flow-step"><span class="step-num">3</span>借阅办理</div>
                <el-icon><ArrowRight /></el-icon>
                <div class="flow-step"><span class="step-num">4</span>AI分析</div>
              </div>
            </div>
          </div>
          <div class="section-cta">
            <el-button type="primary" size="large" round @click="goLogin">立即体验演示环境</el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== FAQ ========== -->
    <section id="faq" class="faq-section">
      <div class="container">
        <h2 class="section-title">常见问题</h2>
        <div class="faq-list">
          <div v-for="(faq, idx) in faqs" :key="idx" class="faq-item" :class="{ active: faq.open }">
            <div class="faq-question" @click="toggleFaq(idx)">
              <span>{{ faq.q }}</span>
              <el-icon :class="{ rotated: faq.open }"><ArrowDown /></el-icon>
            </div>
            <div class="faq-answer" v-show="faq.open">
              <p>{{ faq.a }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== FOOTER ========== -->
    <footer class="site-footer">
      <div class="container">
        <div class="footer-grid">
          <div class="footer-col">
            <h4>产品</h4>
            <a href="#" @click.prevent="scrollTo('#features')">功能清单</a>
            <a href="#" @click.prevent="scrollTo('#tech')">技术文档</a>
            <a href="#">更新日志</a>
          </div>
          <div class="footer-col">
            <h4>资源</h4>
            <a href="#" @click.prevent="scrollTo('#tech')">部署指南</a>
            <a href="#">API文档</a>
            <a href="#" @click.prevent="scrollTo('#faq')">常见问题</a>
          </div>
          <div class="footer-col">
            <h4>演示</h4>
            <a href="#" @click.prevent="goLogin">在线体验</a>
            <a href="#">测试账号</a>
          </div>
          <div class="footer-col">
            <h4>关于</h4>
            <p>智慧图书馆 AI 数据分析系统</p>
            <p>基于 Spring Boot + Vue 3 构建</p>
          </div>
        </div>
        <div class="footer-bottom">
          <div class="footer-tech-stack">
            <span>技术栈：</span>
            <el-tag size="small">Spring Boot</el-tag>
            <el-tag size="small" type="success">Vue</el-tag>
            <el-tag size="small" type="warning">MySQL</el-tag>
            <el-tag size="small" type="info">Redis</el-tag>
          </div>
          <p class="footer-copy">© 2026 智慧图书馆系统. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const scrolled = ref(false)

const faqs = ref([
  { q: '是否需要购买RFID或扫码枪？', a: '不需要。系统为纯软件方案，支持手动输入ID办理借阅。前端提供"模拟扫码"按钮便于演示。', open: false },
  { q: 'AI分析需要联网调用大模型吗？', a: '不需要。AI模块基于规则引擎实现，通过SQL统计+条件判断生成分析结论，离线环境可用，零API成本。', open: false },
  { q: '数据存储在哪里？安全吗？', a: '数据存储在您本地的MySQL数据库中，文件（封面/头像）存储在服务器本地磁盘。系统使用JWT+BCrypt加密，数据完全自主可控。', open: false },
  { q: '能否对接学校现有的教务系统？', a: '系统提供标准RESTful API和统一响应格式，具备对接基础。具体对接需根据教务系统接口文档二次开发。', open: false },
  { q: '支持多少藏书量和并发用户？', a: '基于Spring Boot + MyBatis-Plus + Redis缓存，支持万级馆藏和百级并发。超大规模场景需分库分表改造。', open: false },
  { q: '开源吗？可以二次开发吗？', a: '系统采用标准前后端分离架构，代码结构清晰（com.library.smart包规范），注释完整，具备二次开发基础。', open: false }
])

const handleScroll = () => {
  scrolled.value = window.scrollY > 60
}

const toggleFaq = (idx: number) => {
  faqs.value[idx].open = !faqs.value[idx].open
}

const scrollTo = (selector: string) => {
  const el = document.querySelector(selector)
  if (el) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

const goLogin = () => {
  router.push('/login')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* ===== RESET & BASE ===== */
.landing-page {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  color: #333;
  overflow-x: hidden;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
.section-title {
  font-size: 32px;
  font-weight: 700;
  text-align: center;
  margin-bottom: 48px;
  color: #1a1a2e;
}
.section-cta {
  text-align: center;
  margin-top: 40px;
}

/* ===== HEADER ===== */
.site-header {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 1000;
  background: rgba(255,255,255,0.95);
  backdrop-filter: blur(10px);
  transition: all 0.3s;
  border-bottom: 1px solid transparent;
}
.site-header.header-scrolled {
  border-bottom-color: #e8e8e8;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 8px;
}
.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
}
.main-nav {
  display: flex;
  gap: 32px;
}
.main-nav a {
  color: #555;
  text-decoration: none;
  font-size: 15px;
  font-weight: 500;
  transition: color 0.2s;
}
.main-nav a:hover {
  color: #409EFF;
}

/* ===== HERO ===== */
.hero-section {
  padding: 140px 0 0;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
  position: relative;
  overflow: hidden;
}
.hero-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}
.hero-title {
  font-size: 44px;
  font-weight: 800;
  color: #1a1a2e;
  line-height: 1.2;
  margin-bottom: 20px;
}
.hero-subtitle {
  font-size: 18px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 32px;
}
.trust-badges {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 36px;
}
.badge-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #444;
}
.badge-item .el-icon {
  flex-shrink: 0;
}
.hero-cta {
  display: flex;
  gap: 16px;
}
.hero-image {
  display: flex;
  justify-content: center;
}
.hero-screenshot {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15);
  background: #fff;
  transform: perspective(800px) rotateY(-5deg);
  transition: transform 0.5s;
}
.hero-screenshot:hover {
  transform: perspective(800px) rotateY(0deg);
}
.mock-dashboard {
  width: 100%;
}
.mock-header {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 12px 16px;
  background: #f0f2f5;
  border-bottom: 1px solid #e8e8e8;
}
.mock-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}
.mock-title {
  margin-left: 12px;
  font-size: 13px;
  color: #666;
}
.mock-body {
  padding: 20px;
}
.mock-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 20px;
}
.mock-stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  color: #fff;
}
.mock-stat-card:nth-child(2) { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
.mock-stat-card:nth-child(3) { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); }
.mock-stat-card:nth-child(4) { background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%); }
.mock-stat-num { font-size: 24px; font-weight: 700; }
.mock-stat-label { font-size: 12px; opacity: 0.9; }
.mock-charts {
  display: flex;
  gap: 16px;
  align-items: stretch;
}
.mock-chart {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  padding: 12px 0;
  height: 120px;
}
.mock-chart-bar {
  flex: 1;
  background: linear-gradient(to top, #409EFF, #79bbff);
  border-radius: 4px 4px 0 0;
  min-height: 8px;
  transition: height 0.3s;
}
.mock-pie {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: conic-gradient(#409EFF 0% 40%, #67C23A 40% 65%, #E6A23C 65% 85%, #F56C6C 85% 100%);
  margin: 0 auto;
}
.hero-wave {
  margin-top: 60px;
  line-height: 0;
}
.hero-wave svg {
  display: block;
  width: 100%;
  height: 80px;
}

/* ===== PAIN POINTS ===== */
.pain-section {
  padding: 80px 0;
  background: #f8fafc;
}
.pain-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}
.pain-card {
  background: #fff;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  transition: all 0.3s;
}
.pain-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.08);
}
.pain-icon {
  font-size: 36px;
  margin-bottom: 12px;
}
.pain-card h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1a1a2e;
}
.pain-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 20px;
}
.pain-compare {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8fafc;
  border-radius: 8px;
}
.compare-before, .compare-after {
  flex: 1;
  text-align: center;
}
.compare-label {
  font-size: 12px;
  color: #999;
  display: block;
  margin-bottom: 6px;
}
.compare-box {
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 500;
}
.excel-box {
  background: #fff;
  border: 1px solid #e8e8e8;
  color: #999;
}
.dashboard-box {
  background: #ecf5ff;
  color: #409EFF;
}

/* ===== FEATURES ===== */
.features-section {
  padding: 80px 0;
  background: #fff;
}
.features-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}
.feature-card {
  background: #f8fafc;
  border-radius: 16px;
  padding: 28px;
  transition: all 0.3s;
}
.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.06);
}
.feature-card-wide {
  grid-column: 1 / -1;
}
.feature-icon {
  font-size: 36px;
  margin-bottom: 12px;
}
.feature-card h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1a1a2e;
}
.feature-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 12px;
}
.feature-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 16px;
}
.feature-ai-badge {
  margin-bottom: 12px;
}
.feature-mock {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e8e8e8;
}
.mock-table {
  font-size: 12px;
}
.mock-table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  padding: 6px 0;
  border-bottom: 1px solid #f0f0f0;
  gap: 4px;
}
.mock-table-row.header {
  font-weight: 600;
  color: #999;
  font-size: 11px;
}
.mock-tag { padding: 1px 6px; border-radius: 3px; font-size: 11px; }
.mock-tag.t1 { background: #ecf5ff; color: #409EFF; }
.mock-tag.t2 { background: #fef0f0; color: #F56C6C; }
.mock-status { padding: 1px 6px; border-radius: 3px; font-size: 11px; }
.mock-status.available { background: #f0f9eb; color: #67C23A; }
.mock-status.empty { background: #fef0f0; color: #F56C6C; }
.mock-tabs {
  display: flex;
  gap: 0;
  margin-bottom: 12px;
  border-bottom: 2px solid #e8e8e8;
}
.mock-tab {
  padding: 6px 16px;
  font-size: 13px;
  cursor: default;
  color: #999;
}
.mock-tab.active {
  color: #409EFF;
  border-bottom: 2px solid #409EFF;
  margin-bottom: -2px;
}
.mock-form {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.mock-input {
  display: flex;
  justify-content: space-between;
  padding: 6px 8px;
  background: #f8fafc;
  border-radius: 4px;
  font-size: 13px;
}
.mock-input label { color: #999; }
.mock-btn {
  padding: 6px;
  background: #409EFF;
  color: #fff;
  border-radius: 4px;
  font-size: 13px;
  text-align: center;
  cursor: default;
}
.mock-line-chart {
  height: 100px;
  position: relative;
  margin-bottom: 12px;
}
.mock-line {
  position: absolute;
  bottom: 20px;
  left: 0; right: 0;
  height: 50px;
  background: linear-gradient(180deg, transparent 0%, rgba(64,158,255,0.1) 100%);
  border-radius: 4px;
  overflow: hidden;
}
.mock-line::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0; right: 0;
  height: 3px;
  background: #409EFF;
  border-radius: 2px;
  clip-path: polygon(0% 60%, 5% 30%, 10% 80%, 15% 20%, 20% 70%, 25% 10%, 30% 50%, 35% 40%, 40% 90%, 45% 30%, 50% 60%, 55% 20%, 60% 80%, 65% 40%, 70% 70%, 75% 10%, 80% 50%, 85% 30%, 90% 60%, 95% 20%, 100% 50%);
}
.mock-peak-label {
  position: absolute;
  font-size: 11px;
  color: #67C23A;
  bottom: 70px;
  left: 15%;
}
.mock-peak-label.valley {
  color: #F56C6C;
  left: 55%;
  bottom: 10px;
}
.mock-ai-card {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #fdf6ec;
  border: 1px solid #faecd8;
  border-radius: 6px;
  font-size: 12px;
  color: #666;
}
.mock-rec-tabs {
  display: flex;
  gap: 0;
  margin-bottom: 12px;
  border-bottom: 2px solid #e8e8e8;
}
.mock-rec-tab {
  padding: 6px 16px;
  font-size: 13px;
  cursor: default;
  color: #999;
}
.mock-rec-tab.active {
  color: #409EFF;
  border-bottom: 2px solid #409EFF;
  margin-bottom: -2px;
}
.mock-rec-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.mock-rec-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  padding: 4px 0;
  border-bottom: 1px solid #f5f5f5;
}
.mock-confidence {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  color: #67C23A;
  min-width: 80px;
}
.mock-bar {
  height: 6px;
  background: linear-gradient(to right, #67C23A, #95d475);
  border-radius: 3px;
}
.mock-report-btn {
  padding: 8px 16px;
  background: #409EFF;
  color: #fff;
  border-radius: 6px;
  font-size: 14px;
  text-align: center;
  cursor: default;
  margin-bottom: 12px;
}
.mock-report-preview {
  background: #f8fafc;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  padding: 12px;
}
.mock-report-title {
  font-size: 13px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 8px;
}
.mock-report-line {
  font-size: 12px;
  color: #666;
  line-height: 1.8;
}

/* ===== TECH ===== */
.tech-section {
  padding: 80px 0;
  background: #f8fafc;
}
.tech-arch {
  margin-bottom: 48px;
}
.arch-flow {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 24px;
}
.arch-node {
  background: #fff;
  border: 2px solid #e8e8e8;
  border-radius: 12px;
  padding: 16px 24px;
  text-align: center;
  transition: all 0.3s;
}
.arch-node:hover {
  border-color: #409EFF;
  box-shadow: 0 4px 12px rgba(64,158,255,0.15);
}
.arch-label {
  display: block;
  font-weight: 600;
  font-size: 15px;
  color: #1a1a2e;
}
.arch-node small {
  font-size: 11px;
  color: #999;
}
.arch-node.db-node {
  border-color: #67C23A;
}
.arch-node.db-node:hover {
  border-color: #67C23A;
  box-shadow: 0 4px 12px rgba(103,194,58,0.15);
}
.arch-arrow {
  font-size: 20px;
  color: #409EFF;
}
.tech-tags {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px;
}
.compare-title {
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #1a1a2e;
}
.compare-table-wrapper {
  overflow-x: auto;
}
.compare-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}
.compare-table th, .compare-table td {
  padding: 14px 20px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
}
.compare-table th {
  background: #f8fafc;
  font-weight: 600;
  color: #555;
}
.compare-table th.highlight, .compare-table td.highlight {
  background: #ecf5ff;
  color: #409EFF;
  font-weight: 500;
}
.compare-table tbody tr:hover {
  background: #fafafa;
}

/* ===== USE CASES ===== */
.usecase-section {
  padding: 80px 0;
  background: #fff;
}
.usecase-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}
.usecase-card {
  background: #f8fafc;
  border-radius: 16px;
  padding: 32px;
  text-align: center;
  transition: all 0.3s;
}
.usecase-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.06);
}
.usecase-icon {
  font-size: 48px;
  margin-bottom: 16px;
}
.usecase-card h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #1a1a2e;
}
.usecase-card p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

/* ===== DEMO ===== */
.demo-section {
  padding: 80px 0;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
}
.demo-content {
  max-width: 600px;
  margin: 0 auto;
}
.demo-info {
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  margin-bottom: 24px;
}
.demo-info h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: #1a1a2e;
}
.account-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}
.account-item code {
  padding: 4px 12px;
  background: #f8fafc;
  border-radius: 4px;
  font-size: 13px;
  color: #555;
}
.demo-flow {
  margin-top: 24px;
}
.flow-steps {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.flow-step {
  display: flex;
  align-items: center;
  gap: 6px;
}
.step-num {
  width: 24px;
  height: 24px;
  background: #409EFF;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
}

/* ===== FAQ ===== */
.faq-section {
  padding: 80px 0;
  background: #fff;
}
.faq-list {
  max-width: 800px;
  margin: 0 auto;
}
.faq-item {
  border: 1px solid #e8e8e8;
  border-radius: 12px;
  margin-bottom: 12px;
  overflow: hidden;
  transition: all 0.3s;
}
.faq-item.active {
  border-color: #409EFF;
  box-shadow: 0 2px 8px rgba(64,158,255,0.1);
}
.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  cursor: pointer;
  font-size: 15px;
  font-weight: 500;
  color: #1a1a2e;
  user-select: none;
}
.faq-question .el-icon {
  transition: transform 0.3s;
}
.faq-question .el-icon.rotated {
  transform: rotate(180deg);
}
.faq-answer {
  padding: 0 20px 16px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

/* ===== FOOTER ===== */
.site-footer {
  background: #1a1a2e;
  color: #ccc;
  padding: 48px 0 24px;
}
.footer-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
  margin-bottom: 32px;
}
.footer-col h4 {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}
.footer-col a {
  display: block;
  color: #999;
  text-decoration: none;
  font-size: 14px;
  margin-bottom: 8px;
  transition: color 0.2s;
}
.footer-col a:hover {
  color: #409EFF;
}
.footer-col p {
  font-size: 14px;
  color: #999;
  line-height: 1.6;
  margin-bottom: 4px;
}
.footer-bottom {
  border-top: 1px solid #2a2a4e;
  padding-top: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}
.footer-tech-stack {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #999;
}
.footer-copy {
  font-size: 13px;
  color: #666;
}

/* ===== RESPONSIVE ===== */
@media (max-width: 768px) {
  .hero-grid { grid-template-columns: 1fr; gap: 32px; }
  .hero-title { font-size: 32px; }
  .trust-badges { grid-template-columns: 1fr; }
  .pain-grid { grid-template-columns: 1fr; }
  .features-grid { grid-template-columns: 1fr; }
  .usecase-grid { grid-template-columns: 1fr; }
  .footer-grid { grid-template-columns: repeat(2, 1fr); }
  .main-nav { display: none; }
  .mock-stats { grid-template-columns: repeat(2, 1fr); }
  .arch-flow { flex-direction: column; }
  .arch-arrow { transform: rotate(90deg); }
  .flow-steps { flex-direction: column; }
  .hero-screenshot { transform: none; }
  .compare-table th, .compare-table td { padding: 10px 12px; font-size: 12px; }
}
</style>