package com.example.data.remote

import com.example.model.Audience
import com.example.model.ContentLanguage
import com.example.model.ContentPackage
import com.example.model.Platform
import com.example.model.ThumbnailStyle
import com.example.model.Tone
import org.json.JSONObject

object ViralContentGenerator {

    suspend fun generatePackage(
        topic: String,
        platform: Platform,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        thumbnailStyle: ThumbnailStyle = ThumbnailStyle.VIRAL_YOUTUBE
    ): ContentPackage {
        val cleanTopic = topic.trim()

        // 1. Try Gemini API first if configured
        if (GeminiApiService.hasValidApiKey()) {
            val systemPrompt = """
                You are a world-class viral video content strategist and YouTube/TikTok/Instagram/Facebook growth expert for "Get 1M Views".
                Your mission: Transform the user's idea into an explosive, high-converting, platform-tailored viral content package.
                Selected Platform: ${platform.titleEn}
                Language: ${language.labelAr}
                Tone: ${tone.labelAr}
                Target Audience: ${audience.labelAr}
                Thumbnail Visual Style: ${thumbnailStyle.labelEn}
                
                Respond ONLY with a valid JSON object matching this schema:
                {
                  "titles": ["title1", "title2", "title3", "title4", "title5"],
                  "recommendedTitle": "best title",
                  "hooks": ["hook1", "hook2", "hook3", "hook4", "hook5"],
                  "primaryHook": "first 5-10s hook",
                  "description": "rich description with timestamps and keywords",
                  "script": "full structured script with visual directions [Visual Cue] and [Voiceover]",
                  "cta": "high-converting call to action",
                  "tags": ["tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8"],
                  "hashtags": ["#tag1", "#tag2", "#tag3", "#tag4", "#tag5"],
                  "keywords": ["keyword1", "keyword2", "keyword3", "keyword4"],
                  "thumbnailText": "2-6 words punchy text",
                  "thumbnailPrompt": "detailed 16:9 prompt including Subject, Background, Lighting, Composition, Camera Angle, Depth of Field, Visual Hook, Typography Placement, Color Direction, 16:9, High Quality",
                  "extraAngleIdeas": ["angle1", "angle2", "angle3"],
                  "captionVariations": ["caption1", "caption2", "caption3"],
                  "commentTriggerQuestion": "question that triggers 100+ comments",
                  "videoDurationAdvice": "e.g. 28-35 seconds for maximum retention",
                  "coverIdea": "visual concept for reel/tiktok cover"
                }
            """.trimIndent()

            val userPrompt = """
                Topic: "$cleanTopic"
                Generate the complete viral content package for ${platform.titleEn}. Follow all platform nuances strictly.
            """.trimIndent()

            val rawJson = GeminiApiService.generateContent(userPrompt, systemPrompt)
            if (!rawJson.isNullOrBlank()) {
                val parsed = parseJsonContent(rawJson, cleanTopic, platform, language, tone, audience, thumbnailStyle)
                if (parsed != null) {
                    return parsed
                }
            }
        }

        // 2. High-fidelity algorithmic generator fallback
        return generateSmartContent(cleanTopic, platform, language, tone, audience, thumbnailStyle)
    }

    private fun parseJsonContent(
        raw: String,
        topic: String,
        platform: Platform,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        style: ThumbnailStyle
    ): ContentPackage? {
        return try {
            val cleaned = raw.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val obj = JSONObject(cleaned)

            val titlesArr = obj.optJSONArray("titles")
            val titles = mutableListOf<String>()
            if (titlesArr != null) {
                for (i in 0 until titlesArr.length()) {
                    titles.add(titlesArr.getString(i))
                }
            }

            val hooksArr = obj.optJSONArray("hooks")
            val hooks = mutableListOf<String>()
            if (hooksArr != null) {
                for (i in 0 until hooksArr.length()) {
                    hooks.add(hooksArr.getString(i))
                }
            }

            val tagsArr = obj.optJSONArray("tags")
            val tags = mutableListOf<String>()
            if (tagsArr != null) {
                for (i in 0 until tagsArr.length()) {
                    tags.add(tagsArr.getString(i))
                }
            }

            val hashtagsArr = obj.optJSONArray("hashtags")
            val hashtags = mutableListOf<String>()
            if (hashtagsArr != null) {
                for (i in 0 until hashtagsArr.length()) {
                    hashtags.add(hashtagsArr.getString(i))
                }
            }

            val keywordsArr = obj.optJSONArray("keywords")
            val keywords = mutableListOf<String>()
            if (keywordsArr != null) {
                for (i in 0 until keywordsArr.length()) {
                    keywords.add(keywordsArr.getString(i))
                }
            }

            val extraAnglesArr = obj.optJSONArray("extraAngleIdeas")
            val extraAngles = mutableListOf<String>()
            if (extraAnglesArr != null) {
                for (i in 0 until extraAnglesArr.length()) {
                    extraAngles.add(extraAnglesArr.getString(i))
                }
            }

            val captionVarArr = obj.optJSONArray("captionVariations")
            val captionVars = mutableListOf<String>()
            if (captionVarArr != null) {
                for (i in 0 until captionVarArr.length()) {
                    captionVars.add(captionVarArr.getString(i))
                }
            }

            ContentPackage(
                topic = topic,
                platform = platform,
                language = language,
                tone = tone,
                audience = audience,
                timestamp = System.currentTimeMillis(),
                titles = if (titles.isNotEmpty()) titles else listOf(topic),
                recommendedTitle = obj.optString("recommendedTitle", titles.firstOrNull() ?: topic),
                hooks = hooks,
                primaryHook = obj.optString("primaryHook", hooks.firstOrNull() ?: ""),
                description = obj.optString("description", ""),
                script = obj.optString("script", ""),
                cta = obj.optString("cta", ""),
                tags = tags,
                hashtags = hashtags,
                keywords = keywords,
                thumbnailPrompt = obj.optString("thumbnailPrompt", ""),
                thumbnailText = obj.optString("thumbnailText", ""),
                thumbnailStyle = style,
                extraAngleIdeas = extraAngles,
                captionVariations = captionVars,
                commentTriggerQuestion = obj.optString("commentTriggerQuestion", ""),
                videoDurationAdvice = obj.optString("videoDurationAdvice", ""),
                coverIdea = obj.optString("coverIdea", "")
            )
        } catch (e: Exception) {
            null
        }
    }

    fun generateSmartContent(
        topic: String,
        platform: Platform,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        style: ThumbnailStyle
    ): ContentPackage {
        return when (platform) {
            Platform.YOUTUBE -> generateYouTubePackage(topic, language, tone, audience, style)
            Platform.TIKTOK -> generateTikTokPackage(topic, language, tone, audience, style)
            Platform.INSTAGRAM -> generateInstagramPackage(topic, language, tone, audience, style)
            Platform.FACEBOOK -> generateFacebookPackage(topic, language, tone, audience, style)
        }
    }

    private fun generateYouTubePackage(
        topic: String,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        style: ThumbnailStyle
    ): ContentPackage {
        val isAr = language == ContentLanguage.ARABIC || language == ContentLanguage.MOROCCAN_DARIJA
        val isDarija = language == ContentLanguage.MOROCCAN_DARIJA

        val titles = if (isDarija) {
            listOf(
                "أسرار خطيرة فـ $topic لي ما باغي تا واحد يقولها ليك!",
                "جربت $topic لمدة 30 يوم والنتيجة صدماتني بزاف!!",
                "كيفاش تبدل حياتك مع $topic (طريقة ساهلة ومضمونة)",
                "الحقيقة الكاملة على $topic لي كيخبيوها عليك",
                "أفضل دليل فالمغرب لـ $topic خطوة بخطوة من الصفر"
            )
        } else if (isAr) {
            listOf(
                "أسرار صادمة حول $topic لم يخبرك بها أحد من قبل!",
                "جربت $topic لمدة 30 يوماً متواصلة.. وكانت هذه النتيجة الصادمة!",
                "الدليل الشامل لإتقان $topic من الصفر وحتى الاحتراف (2026)",
                "لماذا يفشل 95% من الناس في $topic؟ وكيف تكون من الـ 5% الناجحين؟",
                "الحقيقة المخفية عن $topic التي ستغير طريقة تفكيرك إلى الأبد!"
            )
        } else {
            listOf(
                "The Shocking Truth About $topic Nobody Told You!",
                "I Tested $topic for 30 Days and THIS Happened...",
                "The Ultimate Masterclass on $topic (Step-by-Step 2026)",
                "Why 99% Fail at $topic (And How to Actually Win)",
                "Stop Doing $topic The Wrong Way! Watch This First"
            )
        }

        val recommendedTitle = titles[1]

        val hooks = if (isDarija) {
            listOf(
                "واش فراسك بلي كلشي لي سمعتيه على $topic كان غالط؟ فهاد الفيديو غانكشف ليك الحقيقة كاملة فـ 5 دقايق!",
                "عطيني غير 30 ثانية من وقتك، وغادي نوريك كيفاش $topic يقدر يبدل مستقبلك للأحسن!",
                "قبل ما تبدا أي حاجة فـ $topic، خاصك تعرف هاد الغلط القاتل لي كيوقع فيه الأغلبية!"
            )
        } else if (isAr) {
            listOf(
                "توقف للحظة! إذا كنت تعتقد أنك تعرف كل شيء عن $topic، فهذا الفيديو سيغير قناعاتك تماماً في أول 60 ثانية.",
                "أكبر سر وراء النجاح في $topic لا يعتمد على الحظ، بل على قاعدة واحدة يتجاهلها 99% من الناس!",
                "في الـ 7 دقائق القادمة، سأكشف لك بالتفصيل كيف تبدأ في $topic وتحقق نتائج خيالية بأقل مجهود ممكن."
            )
        } else {
            listOf(
                "Stop scrolling! If you think you understand $topic, what I'm about to show you will flip everything upside down.",
                "Here is the single biggest secret about $topic that the top 1% never share publicly.",
                "Give me 5 minutes, and I promise you will master $topic faster than anyone else in 2026."
            )
        }

        val description = if (isDarija) {
            """
                مرحباً بكم فـ هاد الفيديو الاستثنائي لي غادي نهدرو فيه بالتفصيل على $topic وكيفاش تستافد منو إلى أقصى حد!
                
                📌 فهاد الفيديو غادي تكتشف:
                00:00 - المقدمة والسر الصادم
                01:30 - المشكل الحقيقي وراء $topic
                03:45 - 3 خطوات عملية للتطبيق الفوري
                06:15 - كيفاش تتفادى الأخطاء الشائعة
                08:30 - الخلاصة والنصيحة الذهبية
                
                🔔 لا تنسى الاشتراك في القناة وتفعيل زر الجرس (🛎️) باش يوصلك كل جديد ومفيد!
                💬 شاركنا رأيك في التعليقات: واش سبق ليك جربتي هاد الطريقة؟
                
                #Get1MViews #Viral #$topic
            """.trimIndent()
        } else if (isAr) {
            """
                في هذا الفيديو الحصري، نكشف الستار عن أسرار وخفايا $topic وكيف يمكنك استغلاله لتحقيق أقصى نجاح ممكن في عام 2026.
                
                ⏱️ الفصول الزمنية (Timestamps):
                00:00 - الصدمة الأولى: الحقيقة غير المعلنة
                01:15 - لماذا يتجاهل الجميع أهم خطوة في $topic؟
                03:40 - التحليل العملي واستراتيجيات التطبيق الواقعي
                06:20 - أخطاء كارثية يجب تجنبها فوراً
                08:50 - خطة العمل الخماسية لتحقيق النجاح الفيروسي
                
                🎯 روابط ومصادر إضافية:
                - لا تنسَ الاشتراك في القناة وتفعيل جرس التنبيهات 🔔 لتصلك فيديوهاتنا الحصرية أسبوعياً.
                - اترك لنا تعليقاً بسؤالك حول $topic وسنجيبك في الحال!
                
                #يوتيوب #محتوى_فيروسي #$topic #Get1MViews
            """.trimIndent()
        } else {
            """
                In this video, we break down everything you need to know about $topic, from insider strategies to actionable step-by-step tactics in 2026.
                
                ⏱️ TIMESTAMPS:
                00:00 - The Untold Secret
                01:25 - The Core Problem with $topic
                03:50 - 3 Actionable Steps to Take Today
                06:40 - Costly Mistakes You Must Avoid
                09:10 - Final Verdict & Challenge
                
                👉 Make sure to SUBSCRIBE and turn on notifications (🔔) so you never miss another viral breakthrough!
                Leave a comment below sharing your biggest takeaway!
            """.trimIndent()
        }

        val script = if (isAr) {
            """
                [المشهد 01: الهوك البصري - أول 5 ثوانٍ]
                (الكاميرا: زاوية قريبة سريعة، المذيع ينظر بحماس مباشر للعدسة)
                المذيع: "لو قلت لك إن 90% من كل ما سمعته عن $topic هو مجرد تكرار لمعلومات خاطئة.. هل ستصدقني؟"
                (مؤثر صوتي قوي Whoosh + ظهور عنوان تشويقي على الشاشة)
                
                [المشهد 02: المقدمة وتحديد المشكلة - 00:05 إلى 00:45]
                المذيع: "الجميع يريد تحقيق نتائج استثنائية، لكن المشكلة الحقيقية هي أن الطرق التقليدية لم تعد تجدي نفعاً في 2026. اليوم سنكشف 3 قواعد سرية ستختصر عليك شهوراً من التجربة والخطأ."
                
                [المشهد 03: النقطة الأولى - القوة المحركة - 00:45 إلى 02:30]
                (لقطات B-Roll توضيحية ورسوم بيانية ديناميكية)
                المذيع: "النقطة الأولى تبدأ بفهم الأساس المتين: $topic ليس مجرد اتجاه عابر، بل هو فرصة حقيقية إذا أدركت زاوية التفرد."
                
                [المشهد 04: النقطة الثانية - التحليل العملي - 02:30 إلى 04:30]
                المذيع: "ثانياً، تجنب هذا الخطأ الشائع: لا تحاول تقليد الآخرين، بل طبق هذه الاستراتيجية المباشرة..."
                
                [المشهد 05: ذروة الفيديو والدعوة للإجراء - 04:30 إلى النهاية]
                المذيع: "والآن السؤال لك: هل أنت مستعد للبدء؟ اضغط زر الإعجاب، واشترك في القناة ليصلك الجزء الثاني، واكتب لي في التعليقات ما هي أكبر عقبة تواجهك!"
            """.trimIndent()
        } else {
            """
                [SCENE 01: THE 5-SECOND HOOK]
                (Camera: Tight snap zoom, speaker looks directly into lens with intense focus)
                Speaker: "What if everything you've been told about $topic was completely upside down?"
                (Sound FX: Fast whoosh + bold on-screen dynamic text)
                
                [SCENE 02: THE TENSION & PROBLEM - 00:05 - 00:45]
                Speaker: "Most creators and people waste months doing it the old way. But today, I am breaking down the exact blueprint that actually works in 2026."
                
                [SCENE 03: CORE VALUE DELIVERY - 00:45 - 03:30]
                (B-roll visuals: dynamic graphs, screen recordings, real-world examples)
                Speaker: "Step one is counterintuitive: when tackling $topic, you have to ignore the noise and focus exclusively on this single leverage point..."
                
                [SCENE 04: THE CLIMAX & CTA - 03:30 - END]
                Speaker: "If you got value from this, smash that Like button, subscribe for weekly breakdowns, and drop your thoughts in the comments below!"
            """.trimIndent()
        }

        val tags = listOf(
            topic,
            "أسرار $topic",
            "كيف تبدأ $topic",
            "شرح $topic 2026",
            "طريقة $topic",
            "نصائح $topic",
            "Get 1M Views",
            "محتوى فيروزي",
            "زيادة المشاهدات",
            "صناعة المحتوى",
            "تطوير الذات"
        )

        val hashtags = listOf(
            "#$topic",
            "#يوتيوب",
            "#محتوى_فيروسي",
            "#Get1MViews",
            "#ترند_اليوم",
            "#صناعة_المحتوى"
        )

        val keywords = listOf(
            topic,
            "أفضل طرق $topic",
            "شرح مبسط لـ $topic",
            "أسرار النجاح في $topic",
            "دليل $topic للمبتدئين 2026"
        )

        val thumbnailPrompt = """
            Subject: Expressive dynamic creator pointing with shock and excitement at a glowing holographic 3D icon representing ($topic), mouth slightly open in awe.
            Background: Modern high-tech studio environment with soft dark teal and warm orange rim lighting, subtle depth blur (f/1.8 bokeh).
            Lighting: High-contrast cinematic key light on subject's face, neon edge highlights separating subject from background.
            Composition: Rule of thirds, subject on the left occupying 40% of the canvas, leaving ample negative space on the right for text.
            Camera Angle: Slightly low angle (hero perspective) looking upward, 35mm wide lens.
            Depth of Field: Shallow depth of field with razor-sharp focus on eyes and facial expression.
            Visual Hook: Ultra high-contrast glowing elements with 1M views viral indicator, extreme vibrancy.
            Typography Placement: Dedicated empty right quadrant reserved for massive bold typography: "$topic".
            Color Direction: Saturated electric cyan, fiery orange, deep obsidian blacks.
            Professional Visual Style: ${style.keywords}, Ultra HD 8k, hyper-detailed render.
            Aspect Ratio: 16:9
            Quality: Masterpiece, High Quality, Trending on ArtStation, Clean composition.
        """.trimIndent()

        val thumbnailText = if (isAr) "صدمة لا تصدق!" else "DON'T MISS THIS!"

        return ContentPackage(
            topic = topic,
            platform = Platform.YOUTUBE,
            language = language,
            tone = tone,
            audience = audience,
            titles = titles,
            recommendedTitle = recommendedTitle,
            hooks = hooks,
            primaryHook = hooks.first(),
            description = description,
            script = script,
            cta = if (isAr) "اشترك في القناة وفعل الجرس واكتب رأيك في التعليقات!" else "Subscribe, ring the bell, and leave your thoughts below!",
            tags = tags,
            hashtags = hashtags,
            keywords = keywords,
            thumbnailPrompt = thumbnailPrompt,
            thumbnailText = thumbnailText,
            thumbnailStyle = style,
            videoDurationAdvice = "8 إلى 12 دقيقة (لتحقيق أعلى متوسط مدة مشاهدة وفتح إعلانات منتصف الفيديو)",
            coverIdea = "لقطة وجه مكبرة بتعبير صدمة مع عنصر ثلاثي الأبعاد مضيء يرمز للموضوع في الجانب الأيمن."
        )
    }

    private fun generateTikTokPackage(
        topic: String,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        style: ThumbnailStyle
    ): ContentPackage {
        val isAr = language == ContentLanguage.ARABIC || language == ContentLanguage.MOROCCAN_DARIJA
        val isDarija = language == ContentLanguage.MOROCCAN_DARIJA

        val hooks = if (isDarija) {
            listOf(
                "هاد السر فـ $topic مخبي عليك وما باغينكش تعيق بيه! 🤯",
                "إلى مازال كدير $topic بهاد الطريقة، راك كتضيع وقتك وفلوسك! ❌",
                "أسرع طريقة غاتخليك تفركع فـ $topic قبل ما يسالي 2026 🚀",
                "جربت هاد الحيلة فـ $topic وها شنو وقع.. بقا حتى اللخر! ⏳",
                "3 ديال الحوايج لو كان عرفتهم من قبل فـ $topic كان غايكون هبال! 💡"
            )
        } else if (isAr) {
            listOf(
                "أتحداك تكون عارف هذا السر الصادم عن $topic قبل اليوم! 🤯",
                "إذا كنت ما زلت تقوم بـ $topic بهذه الطريقة القديمة، فأنت تدمر نتائجك فوراً! ❌",
                "هذا الشيء الواحد حول $topic جعلني أحقق نتائج خيالية في أقل من أسبوع! 🚀",
                "توقف عن التمرير! هذا الفيديو سيوفر عليك شهوراً من التعب في $topic ⏳",
                "3 حيل سرية في $topic لن يخبرك بها المشاهير أبداً! 🤫"
            )
        } else {
            listOf(
                "I bet you didn't know THIS secret about $topic until right now! 🤯",
                "If you're still doing $topic like this, you're literally wasting your time! ❌",
                "This ONE single trick about $topic completely changed everything for me! 🚀",
                "Stop scrolling! This 20-second video will save you weeks on $topic ⏳",
                "3 secret $topic hacks the top creators are gatekeeping from you! 🤫"
            )
        }

        val caption = if (isAr) {
            "السر اللي الكل مخبيه عن $topic! جربها وقولي فالكومنتات إذا كنت عارفها من قبل 👇🔥 #Get1MViews #fyp"
        } else {
            "The truth about $topic nobody wants to admit! Try this today and let me know in the comments 👇🔥 #viral #fyp"
        }

        val hashtags = listOf(
            "#$topic",
            "#fyp",
            "#foryou",
            "#viral",
            "#ترند_تيك_توك",
            "#تيك_توك_عرب",
            "#اكسبلور",
            "#Get1MViews"
        )

        val script = if (isAr) {
            """
                [00:00 - 00:03] (هوك بصري سريع: توجيه الإصبع للشاشة + صوت تنبيه)
                "أكبر غلطة كيديروها الناس فـ $topic هي هادي!"
                
                [00:03 - 00:15] (انتقال سريع Jump Cut + لقطات عملية سريعة)
                "عوض ما تضيع وقتك فالطريقة العادية، جرب هاد التكنيك:
                1. ركز على النتيجة الأولى فـ 24 ساعة.
                2. بسط الخطوات واستعمل أدوات الذكاء الاصطناعي المجانية."
                
                [00:15 - 00:25] (الذروة مع نص متحرك كبير)
                "النتيجة غاتصدمك وغاتشوف فرق كبير من أول تجربة!"
                
                [00:25 - 00:30] (دعوة سريعة للإجراء CTA)
                "دير لايك وحفظ للفيديو باش ترجع ليه، وشاركنا رأيك فالتعليقات!"
            """.trimIndent()
        } else {
            """
                [00:00 - 00:03] (Hook: Instant eye contact, finger tap on camera glass)
                "The biggest mistake everyone makes with $topic is THIS!"
                
                [00:03 - 00:18] (Fast jump cuts with bold text popups)
                "Instead of doing it the hard way, do this instead:
                1. Strip away the fluff.
                2. Use this 2026 smart shortcut."
                
                [00:18 - 00:28] (Value drop)
                "You'll see 10x better results in half the time, guaranteed."
                
                [00:28 - 00:32] (CTA)
                "Save this video before it gets lost, and follow for more daily viral secrets!"
            """.trimIndent()
        }

        val extraAngleIdeas = listOf(
            "زاوية المقارنة: قبل وبعد استخدام الطريقة الصحيحة لـ $topic",
            "زاوية التحدي: هل يمكنك إتقان $topic خلال 7 أيام فقط؟",
            "زاوية الأخطاء الفادحة: أكثر 3 أخطاء شائعة تقع فيها عند بداية $topic"
        )

        val coverPrompt = """
            Subject: Ultra expressive TikTok creator with an intense surprised reaction, holding a high-tech glowing device displaying ($topic).
            Background: Neon aesthetic vertical lighting with dark studio backdrop, purple and electric cyan rim glow.
            Composition: Centered vertical 9:16 layout, leaving the upper and lower thirds clean for TikTok UI overlays.
            Visual Hook: Sharp eyes, high contrast, bold 3D sticker effect.
            Color Palette: Cyber cyan, hot magenta, obsidian black.
            Style: ${style.keywords}, High Quality TikTok thumbnail.
        """.trimIndent()

        return ContentPackage(
            topic = topic,
            platform = Platform.TIKTOK,
            language = language,
            tone = tone,
            audience = audience,
            titles = hooks,
            recommendedTitle = hooks[0],
            hooks = hooks,
            primaryHook = hooks[0],
            description = caption,
            script = script,
            cta = if (isAr) "تابع الحساب للمزيد من الأسرار واحفظ الفيديو للرجوع إليه!" else "Follow for more daily viral drops and save this video!",
            tags = listOf(topic, "fyp", "viral", "ترند", "تيك_توك"),
            hashtags = hashtags,
            keywords = listOf(topic, "تيك توك $topic", "حيل $topic السريعة"),
            thumbnailPrompt = coverPrompt,
            thumbnailText = if (isAr) "سر لا يصدق!" else "SECRET REVEALED",
            thumbnailStyle = style,
            extraAngleIdeas = extraAngleIdeas,
            videoDurationAdvice = "25 إلى 34 ثانية (المدة المثالية لنسبة إكمال تتجاوز 85% لرفع الفيديو في خورازمية For You)",
            coverIdea = "تعبير صدمة واضح مع يد تشير للأعلى ونص بارز بخط عريض ملون."
        )
    }

    private fun generateInstagramPackage(
        topic: String,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        style: ThumbnailStyle
    ): ContentPackage = ContentPackage(
        topic = topic,
        platform = Platform.INSTAGRAM,
        language = language,
        tone = tone,
        audience = audience,
        titles = listOf(
            "دليل $topic الذي سيوفر عليك ساعات طويلة! ✨",
            "كيف تحول $topic إلى مصدر قوة وإبداع في 2026 🚀",
            "3 أسرار لا يخبرك بها أحد عن $topic 🤫",
            "هل ترتكب هذا الخطأ الشائع في $topic؟ ⚠️",
            "خطوة بخطوة: الطريق الأسهل لاحتراف $topic 📌"
        ),
        recommendedTitle = "دليل $topic الذي سيوفر عليك ساعات طويلة! ✨",
        hooks = listOf(
            "إذا كنت تشعر بالحيرة مع $topic، فهذا البوست صُمم خصيصاً لك!",
            "سر بسيط ولكنه قوي جداً سيغير نتائجك في $topic بدءاً من اليوم.",
            "احفظ هذا المنشور فوراً لأنك ستحتاجه بالتأكيد خلال هذا الأسبوع!"
        ),
        primaryHook = "إذا كنت تشعر بالحيرة مع $topic، فهذا البوست صُمم خصيصاً لك!",
        description = """
            هل فكرت يوماً كيف ينجح المحترفون في $topic بكل سهولة وبدون تعقيد؟ 
            
            السر يكمن في البساطة والتطبيق الذكي. في هذا الدليل السريع، جمعنا لك أهم النقاط الأساسية التي ستحدث الفارق الحقيقي في نتائجك اليومية:
            
            1️⃣ النقطة الأولى: ركز على القيمة وليس على المجهود العشوائي.
            2️⃣ النقطة الثانية: استخدم الأدوات الحديثة لتسريع إنجازك.
            3️⃣ النقطة الثالثة: الاستمرارية والانضباط هما العامل الحاسم دائماً.
            
            💬 شاركنا رأيك: ما هي أهم نصيحة ساعدتك في $topic؟
            🔖 احفظ المنشور لتستفيد منه لاحقاً!
        """.trimIndent(),
        script = """
            [Reel Visual 01 - Hook]:
            (النص على الشاشة بخط أنيق: "3 قواعد ذهبية في $topic لا تتجاهلها")
            المتحدث: "إذا كنت تريد نتائج حقيقية وسريعة، إليك ما يجب أن تفعله فوراً..."
            
            [Reel Visual 02 - Core Tips]:
            (انتقالات أنيقة مع موسيقى هادئة حماسية)
            "القاعدة الأولى: ابدأ صغيراً واستمر يومياً.
            القاعدة الثانية: استثمر في التعلم العملي.
            القاعدة الثالثة: قس نتائجك وعدل مسارك."
            
            [Reel Visual 03 - Call to Action]:
            "احفظ الريل وشاركه مع صديق مهتم بـ $topic!"
        """.trimIndent(),
        cta = "احفظ الريل (Save) وشاركنا رأيك في التعليقات!",
        tags = listOf(topic, "انستقرام", "ريلز", "تطوير", "نجاح"),
        hashtags = listOf(
            "#$topic",
            "#ريلز_انستغرام",
            "#اكسبلور_فولو",
            "#انستقرام_عربي",
            "#reels",
            "#instagramreels",
            "#viralpost"
        ),
        keywords = listOf(topic, "ريلز $topic", "كابشن $topic", "نصائح انستقرام"),
        thumbnailPrompt = """
            Subject: Aesthetic modern content creator in a beautifully lit minimalist workspace, holding a sleek smartphone showing ($topic) graphics.
            Background: Clean Scandinavian interior with warm ambient lamp, subtle indoor plants, soft beige and gold tones.
            Lighting: Soft diffused cinematic daylight mixed with warm indoor golden hour practical lights.
            Composition: Clean vertical 9:16 layout, elegant framing with magazine cover aesthetic.
            Visual Hook: Premium lifestyle vibe, crisp typography space, high aesthetic value.
            Color Palette: Warm terracotta, champagne gold, clean white, slate grey.
            Style: ${style.keywords}, Instagram aesthetic, high fashion editorial lighting.
        """.trimIndent(),
        thumbnailText = "دليل احترافي 2026",
        thumbnailStyle = style,
        captionVariations = listOf(
            "النسخة المباشرة والسريعة:\nأهم 3 أسرار لتتقن $topic في 2026. احفظ المنشور وطبق الخطوات فوراً! 🚀 #$topic #reels",
            "نسخة سرد القصة (Storytelling):\nقبل سنة من الآن، كنت أعاني مع $topic وأشعر بالإحباط، حتى اكتشفت هذه القاعدة البسيطة التي غيرت كل شيء. اقرأ التفاصيل في البوست وشاركني تجربتك! ✨ #$topic",
            "نسخة القيمة التعليمية المركزة:\nدليل شامل ومختصر: كيف تحقق أفضل نتائج في $topic بدون تعقيد؟ 1. التخطيط 2. الأدوات الذكية 3. المراجعة المستمرة. احفظه الآن! 📌 #$topic"
        ),
        videoDurationAdvice = "15 إلى 30 ثانية (لريلز انستقرام لتعزيز التكرار وإعادة المشاهدة)",
        coverIdea = "صورة جمالية للمتحدث مع إطار بسيط وعنوان أنيق بخط فخم وواضح."
    )

    private fun generateFacebookPackage(
        topic: String,
        language: ContentLanguage,
        tone: Tone,
        audience: Audience,
        style: ThumbnailStyle
    ): ContentPackage {
        val postTitle = "قصة وتجربة حقيقية: ما تعلمته عن $topic بعد سنوات من التجربة"

        val description = """
            كثير من الناس يعتقدون أن النجاح في $topic يحتاج إلى ميزانيات ضخمة أو حظ خارق للعادة.. لكن الواقع مختلف تماماً!
            
            خلال الفترة الأخيرة، تعمقت كثيراً في موضوع $topic ووجدت أن الفارق الوحيد بين من يحقق نتائج مذهلة ومن يقف مكانه هو طريقة التفكير واستغلال الفرص في الوقت المناسب.
            
            إليك 3 خلاصات ذهبية قد تغير نظرتك للموضوع:
            1. التجربة الأولى لن تكون مثالية، ولكنها الأساس الذي ستبني عليه.
            2. لا تنتظر الوقت المناسب، لأن أفضل وقت للبدء كان الأمس، وثاني أفضل وقت هو الآن!
            3. المحيطين بك وطريقة تلقيك للمعلومة يصنعون 80% من نجاحك.
            
            أنا مقتنع أن كل شخص قادر على إحداث فارق كبير إذا ركز على الجوهر وابتعد عن المشتتات.
        """.trimIndent()

        val shortVersion = """
            السر الحقيقي وراء $topic ليس الحظ، بل الاستمرارية والبدء بالطريقة الصحيحة دون تعقيد! إذا كنت تفكر في البدء، لا تتردد اليوم. شاركنا رأيك في التعليقات! 💬
        """.trimIndent()

        val commentQuestion = "برأيك الشخصي، ما هو أصعب عائق يمنع الناس من النجاح في $topic؟ هل هي قلة المعرفة أم الخوف من البداية؟ شاركنا في التعليقات!"

        val imagePrompt = """
            Subject: A relatable, engaging scene illustrating the story behind ($topic), featuring genuine emotion and authentic human connection.
            Background: Modern everyday environment with warm natural sunlight, rich environmental storytelling details.
            Lighting: Warm inviting daylight, friendly and approachable tone.
            Composition: 1:1 or 16:9 landscape, perfect for Facebook feed scroll stopping.
            Visual Hook: Natural relatable facial expression, rich color grading, sharp details.
            Style: ${style.keywords}, High quality documentary and community storytelling photography.
        """.trimIndent()

        return ContentPackage(
            topic = topic,
            platform = Platform.FACEBOOK,
            language = language,
            tone = tone,
            audience = audience,
            titles = listOf(
                postTitle,
                "قبل أن تبدأ في $topic.. اقرأ هذه الكلمات بتركيز!",
                "نقاش مفتوح: هل تعتقد أن $topic أصبح أكثر سهولة أم أكثر تعقيداً في 2026؟",
                "درس مجاني من تجارب حقيقية في $topic",
                "الحقيقة التي يتجاهلها الكثيرون حول $topic"
            ),
            recommendedTitle = postTitle,
            hooks = listOf(
                "هل فكرت من قبل لماذا يتحدث الجميع عن $topic هذه الأيام؟",
                "شيء واحد إذا فهمته اليوم عن $topic، سيوفر عليك الكثير من التخبط مستقبلاً.",
                "قصة سريعة ولكنها تحمل درساً كبيراً لكل مهتم بـ $topic..."
            ),
            primaryHook = "هل فكرت من قبل لماذا يتحدث الجميع عن $topic هذه الأيام؟",
            description = description,
            script = "النسخة المختصرة للمنشور:\n$shortVersion",
            cta = "شارك هذا المنشور على صفحتك لتعم الفائدة، واترك لنا تعليقاً برأيك!",
            tags = listOf(topic, "فيسبوك", "نقاش", "تجارب", "مجتمع"),
            hashtags = listOf("#$topic", "#فيسبوك", "#مجتمع_المعرفة", "#نجاح", "#Get1MViews"),
            keywords = listOf(topic, "منشور $topic", "نقاش $topic", "قصص نجاح"),
            thumbnailPrompt = imagePrompt,
            thumbnailText = "تجارب حقيقية 2026",
            thumbnailStyle = style,
            commentTriggerQuestion = commentQuestion,
            coverIdea = "صورة معبرة ذات ألوان دافئة تجذب العين أثناء التصفح مع سؤال عريض في الأسفل."
        )
    }

    fun regenerateSection(
        pkg: ContentPackage,
        sectionType: String
    ): ContentPackage {
        val topic = pkg.topic
        val platform = pkg.platform
        val isAr = pkg.language == ContentLanguage.ARABIC || pkg.language == ContentLanguage.MOROCCAN_DARIJA

        return when (sectionType.lowercase()) {
            "titles" -> {
                val newTitles = listOf(
                    "🔥 [طريقة سرية] كيف تفجر أرقام $topic في 2026؟",
                    "إذا كنت تريد النجاح في $topic، شاهد هذا قبل فوات الأوان!",
                    "أكبر لغز في $topic تم حله أخيراً! (نتائج غير مسبوقة)",
                    "الدليل الفيروسي الشامل: $topic من الصفر إلى مليون مشاهدة",
                    "خطوة واحدة غيرت كل شيء في $topic.. إليك التفاصيل كاملة!"
                )
                pkg.copy(titles = newTitles, recommendedTitle = newTitles[0])
            }
            "hook" -> {
                val newHook = if (isAr) {
                    "انتظر ثانية واحدة! قبل أن تضيع وقتك في $topic، اسمع هذه الحقيقة الصادمة التي ستوفر عليك مئات الساعات!"
                } else {
                    "Wait! Before you waste another second on $topic, hear this shocking truth that will save you hundreds of hours!"
                }
                pkg.copy(primaryHook = newHook, hooks = listOf(newHook) + pkg.hooks.drop(1))
            }
            "description" -> {
                val newDesc = if (isAr) {
                    """
                        ✨ فيديو متقدم ومفصل حول $topic:
                        اكتشف في هذا الدليل كل ما تحتاج لمعرفته خطوة بخطوة للوصول إلى أعلى أداء وانتشار ممكن.
                        
                        📌 النقاط المحورية:
                        - الأساسيات الذهبية
                        - استراتيجيات الانتشار الفيروسي
                        - أدوات الذكاء الاصطناعي المساندة
                        
                        🔔 اشترك في القناة ليصلك كل جديد واكتب رأيك في التعليقات!
                    """.trimIndent()
                } else {
                    """
                        ✨ Advanced master breakdown on $topic:
                        Discover everything you need step-by-step to achieve peak virality and engagement in 2026.
                        
                        📌 Key takeaways:
                        - Golden foundations
                        - High-converting hooks
                        - AI-assisted creator workflows
                        
                        🔔 Subscribe and leave your thoughts below!
                    """.trimIndent()
                }
                pkg.copy(description = newDesc)
            }
            "tags" -> {
                val newTags = listOf(
                    topic,
                    "أفضل محتوى $topic",
                    "ترند $topic",
                    "أسرار فيروسية",
                    "زيادة التفاعل",
                    "خوارزميات 2026",
                    "صناعة محتوى احترافي",
                    "Get 1M Views"
                )
                pkg.copy(tags = newTags)
            }
            "hashtags" -> {
                val newHash = listOf(
                    "#$topic",
                    "#محتوى_فيروسي",
                    "#ترند",
                    "#اكسبلور",
                    "#Get1MViews",
                    "#Viral2026"
                )
                pkg.copy(hashtags = newHash)
            }
            "script" -> {
                val newScript = """
                    [00:00 - 00:08] هوك صادم: "هل تساءلت يوماً لماذا تنجح قنوات معينة في $topic بينما يفشل الباقي؟ السر ليس في المعدات الباهظة، بل في هذه المعادلة البسيطة!"
                    [00:08 - 00:40] المشكلة: "أغلب الناس يبدأون من النهاية. إليك الترتيب الصحيح..."
                    [00:40 - 02:00] الحل العملي: "1. اختر زاوية فريدة. 2. قدم الفائدة في أول 10 ثوانٍ. 3. ادعُ المشاهد للمشاركة بذكاء."
                    [02:00 - النهاية] الخاتمة: "جرب هذه الطريقة اليوم واكتب لي نتيجتك في التعليقات!"
                """.trimIndent()
                pkg.copy(script = newScript)
            }
            "thumbnail" -> {
                val newPrompt = """
                    Subject: High energy creator with an open mouth expression of victory, holding a glowing 1M views trophy, neon reflections on face.
                    Background: Ultra clean dark aesthetic with dramatic diagonal lighting streaks and floating 3D particle elements related to ($topic).
                    Composition: Dynamic Dutch angle, bold rule of thirds, large high-contrast negative space for typography.
                    Aspect Ratio: 16:9
                    Visual Hook: Intense neon rim light, razor sharp hyper-detailed textures, cinema-grade color grading.
                    Style: ${pkg.thumbnailStyle.keywords}, 8K Octane render, masterpiece.
                """.trimIndent()
                pkg.copy(thumbnailPrompt = newPrompt, thumbnailText = "سر 1,000,000 مشاهدة!")
            }
            "cta" -> {
                val newCta = if (isAr) {
                    "إذا أعجبك المحتوى وتريد أفكاراً يومية، اشترك في القناة واحفظ المنشور الآن!"
                } else {
                    "If you found this valuable, follow for daily viral blueprints and save this post now!"
                }
                pkg.copy(cta = newCta)
            }
            else -> pkg
        }
    }
}
