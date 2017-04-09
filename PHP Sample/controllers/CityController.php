<?php
namespace app\controllers;

use app\Core\Application\Command\City\{
    CreateCityCommand,
    DeleteCityCommand,
    UpdateCityCommand
};
use app\Core\Application\Dto\City\CityViewDto;
use app\Core\Application\Command\CommandBusInterface;
use app\Core\Domain\Repository\City\CityReadRepository;
use app\Core\Domain\Repository\Counterparty\CounterpartyReadRepository;
use app\Helper\Form\TypeResolver;
use app\Models\Form\City\{
    CreateCityForm,
    UpdateCityForm
};
use app\Models\Search\City\CitySearch;
use app\Helper\View\Config\Constants;
use yii\filters\AccessControl;
use yii\web\Controller;
use yii\filters\VerbFilter;
use Yii;
use yii\web\NotFoundHttpException;

class CityController extends Controller
{
    public $layout = Constants::LAYOUT_MAIN;

    /**
     * @var CommandBusInterface
     */
    private $commandBus;

    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * @var CounterpartyReadRepository
     */
    private $counterpartyReadRepository;

    /**
     * CityController constructor.
     * @param string $id
     * @param \yii\base\Module $module
     * @param array $config
     * @param CommandBusInterface $commandBus
     * @param CityReadRepository $cityReadRepository
     * @param CounterpartyReadRepository $counterpartyReadRepository
     */
    public function __construct(
        $id,
        $module,
        $config = [],
        CommandBusInterface $commandBus,
        CityReadRepository $cityReadRepository,
        CounterpartyReadRepository $counterpartyReadRepository
    ) {
        $this->commandBus = $commandBus;
        $this->cityReadRepository = $cityReadRepository;
        $this->counterpartyReadRepository = $counterpartyReadRepository;
        parent::__construct($id, $module, $config);
    }

    /**
     * @inheritdoc
     */
    public function behaviors()
    {
        return [
            'access' => [
                'class' => AccessControl::className(),
                'rules' => [
                    [
                        'allow' => true,
                        'roles' => ['manager-coordinator'],
                    ],
                ],
            ],
            'verbs' => [
                'class' => VerbFilter::className(),
                'actions' => [
                    'delete' => ['POST'],
                ],
            ],
        ];
    }

    /**
     * Lists all City models.
     * @return mixed
     */
    public function actionIndex()
    {
        $searchModel = new CitySearch();
        $dataProvider = $searchModel->search(Yii::$app->request->queryParams);
        return $this->render('index', [
            'searchModel' => $searchModel,
            'dataProvider' => $dataProvider,
        ]);
    }

    /**
     * @param $id
     * @return string
     * @throws NotFoundHttpException
     */
    public function actionView($id)
    {
        $entity = $this->cityReadRepository->findById($id);
        if($entity === null) {
            throw new NotFoundHttpException('Такого города не существует.');
        }
        $dto = new CityViewDto($entity);
        return $this->render('view', [
            'dto' => $dto,
        ]);
    }

    /**
     * Creates a new City model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     * @return mixed
     */
    public function actionCreate()
    {
        $model = Yii::createObject(CreateCityForm::className());
        if ($model->load(Yii::$app->request->post()) && $model->validate()) {
            $command = new CreateCityCommand([
                    'name' => TypeResolver::toStrOrNull($model->name),
                    'country' => TypeResolver::toStrOrNull($model->country),
                    'extraCharge' => floatval($model->extraCharge)
                ]
            );
            $this->commandBus->execute($command);
            return $this->redirect(['index']);
        }
        return $this->render('create', [
            'model' => $model,
        ]);
    }

    /**
     * @param $id
     * @return string|\yii\web\Response
     * @throws NotFoundHttpException
     * @throws \yii\base\InvalidConfigException
     */
    public function actionUpdate($id)
    {
        $entity = $this->cityReadRepository->findById($id);
        if($entity === null) {
            throw new NotFoundHttpException('Такого города не существует.');
        }
        $model = Yii::createObject(UpdateCityForm::className());
        $model->defineAttributes($entity);
        if ($model->load(Yii::$app->request->post()) && $model->validate()) {
            $command = new UpdateCityCommand([
                    'id' => TypeResolver::toStrOrNull($id),
                    'name' => TypeResolver::toStrOrNull($model->name),
                    'country' => TypeResolver::toStrOrNull($model->country),
                    'extraCharge' => floatval($model->extraCharge)
                ]
            );
            $this->commandBus->execute($command);
            Yii::$app->getSession()->setFlash('success', 'Город обновлён');
            return $this->redirect(['view', 'id' => $id]);
        }
        return $this->render('update', [
            'model' => $model
        ]);
    }

    /**
     * @param $id
     * @return \yii\web\Response
     */
    public function actionDelete($id)
    {
        if ($this->counterpartyReadRepository->existByCityId($id)) {
            Yii::$app->getSession()->setFlash('error', 'Невозможно удалить город, так как к нему прикреплены контрагенты.');
        } else {
            $this->commandBus->execute(new DeleteCityCommand(TypeResolver::toStrOrNull($id)));
        }
        return $this->redirect(['index']);
    }
}
