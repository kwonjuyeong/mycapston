package com.example.myapplication.Main.Activity


//import com.example.myapplication.Main.Fragment.ChatFragment
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.Main.Fragment.*
import com.example.myapplication.Main.Fragment.BoardFragment.BoardFragment
import com.example.myapplication.Main.Fragment.HomeFragment.HomeFragment
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frag_map.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.ArrayList


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(){
    // MainActivity가 가지고 있는 멤버 변수 선언
    private lateinit var homeFragment: HomeFragment
    private lateinit var mapFragment: CurrentPlaceFragment
    private lateinit var boardFragment : BoardFragment
    //private lateinit var chatFragment: ChatFragment
    private lateinit var settingFragment: SettingFragment
//    private lateinit var boardFragment: BoardFragment

    //private lateinit var photoAdapter: PhotoAdapter //1
    //private var dataList = mutableListOf<DataModel>()   //2

    companion object {
        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavi.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            1
//        )
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_container, homeFragment).commit()


    }
    private fun showActionSnackbar(){
        val actionSnackbar = Snackbar.make(constraintLayout, "설정에서 권한을 허가해주세요.", LENGTH_INDEFINITE)

        actionSnackbar.setAction("확인") {
            Toast.makeText(this@MainActivity, "확인 누름!", Toast.LENGTH_LONG).show()
        }
        actionSnackbar.show()
    }

    //사용자에게 위치정보를 받아와도 되냐고 물어보기
    fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {}
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                showActionSnackbar()
                //makeSnackbar("설정에서 권한을 허가 해주세요.")
                finish()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage("서비스 사용을 위해서 몇가지 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 설정할 수 있습니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }

    override fun onResume() {
        super.onResume()

    }
    //바텀 네비게이션 아이템 클릭 리스너

    private val onBottomNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {

            tedPermission()

            when (it.itemId) {
                R.id.action_home -> {
                    homeFragment = HomeFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, homeFragment).commit()
                }
                R.id.action_Map -> {
                    mapFragment = CurrentPlaceFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, mapFragment).commit()
                }
                R.id.action_board -> {
                    // 사진을 가져올 수 있는지 확인 하는 작업
                    boardFragment = BoardFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, boardFragment).commit()
                }
               /* R.id.action_chat -> {
//                    chatFragment = ChatFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, chatFragment).commit()
                }*/
                R.id.action_setting -> {
                    settingFragment = SettingFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, settingFragment).commit()
                }
            }
            true
        }


}